package server;

import asn1objects.*;
import datatypes.*;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import org.apache.http.util.ByteArrayBuffer;
import server.BackEnd;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Static class with one public method to deal with a single request from
 * a UDP or TCP handler to the server.
 *
 * serverQuery is only public method and handles all of this
 */
public class ServerDecoder {

    public static final int FAILURE = -1;

    /**
     * Takes a database location, desired date format (not controlled by user)
     * and a Decoder and then replies with a byte array containing the outcomes of
     * each successfully decoded server query as a series of successes or failures.
     * After the outcomes, comes either straightforward replies to the request or a
     * reiteration of the request.
     * @param _dbfile location of the database on the local system
     * @param sdf format for dates entered into the database
     * @param dec already created decoder
     * @return response to all client requests
     * @throws SQLException if the database is locked or the location of the data base does
     * not exist
     */
    public static byte[] serverQuery(String _dbfile, SimpleDateFormat sdf, Decoder dec, String ipAddress, int port) throws SQLException {

        LinkedList<Integer> okays = new LinkedList<>();

        int numBytes = 0;
        LinkedList<byte[]> responses = new LinkedList<>();
        Connection conn = null;
        try {
            conn = BackEnd.openConnection(_dbfile);
            ProjectOK projectOK = new ProjectOK();

            while (!dec.isEmptyContainer()) {
                System.out.println(dec.objectLen());
                int ok = 0;
                byte[] response;
                switch (dec.tagVal()) {
                    case ASN1Project.TAGVALUE:
                        Project input = new ASN1Project().decode(dec.getFirstObject(true));
                        input.addPort(port);
                        input.addIP(ipAddress);
                        ok = queryProject(conn, sdf, input);
                        projectOK.addOkay(ok);
                        response = new ASN1Project(input).getEncoder().getBytes();

                        // Procedure for failure to get the project
                        if (ok != 0) {
                            System.out.print("FAIL;");
                        }
                        System.out.println(input);

                        break;
                    case ASN1Take.TAGVALUE:
                        Take tk = new ASN1Take().decode(dec.getFirstObject(true));
                        ok = queryTake(conn, tk);
                        response = new ASN1Take(tk).getEncoder().getBytes();

                        if (ok != 0) {
                            System.out.print("Fail;");
                        }
                        System.out.println(tk);

                        break;
                    case ASN1GetProjects.TAGVALUE:
                        Projects ps = queryGetProjects(conn);
                        response = new ASN1Projects(ps).getEncoder().getBytes();

                        if (ps.getProjectNames().isEmpty()) {
                            System.out.println("FAIL;");
                            ok = FAILURE;
                        }
                        System.out.println(ps);

                        break;
                    case ASN1GetProjectsUnabridged.TAGVALUE:
                        ProjectsAnswer pa = queryGetProjectsUnabridged(conn);
                        response = new ASN1ProjectsAnswer(pa).getEncoder().getBytes();

                        if (pa.getProjects().isEmpty()) {
                            System.out.println("FAIL;");
                            ok = FAILURE;
                        }
                        System.out.println(pa);

                        break;
                    case ASN1GetProject.TAGVALUE:
                        GetProject gp = new ASN1GetProject().decode(dec.getFirstObject(true));
                        Project output = queryGetProject(conn, gp.getName());
                        response = new ASN1Project(output).getEncoder().getBytes();

                        if (output.getTasks().isEmpty()) {
                            System.out.println("FAIL;");
                            ok = FAILURE;
                        }
                        System.out.println(gp);

                        break;
                    default:
                        throw new ASN1DecoderFail("Invalid ASN1 Tag Value");
                }

                if (ok == 0) {
                    byte[] okay = new ASN1ProjectOK(0).getEncoder().getBytes();
                    responses.add(okay);
                    numBytes += okay.length;
                }
                numBytes += response.length;
                responses.add(response);
                okays.add(ok);
            }

        } catch (ASN1DecoderFail e) {
            System.out.println("Poor ASN1 message sent");
            e.printStackTrace();
        } finally {
            BackEnd.closeConnection(conn);
        }

        ByteArrayBuffer baf = new ByteArrayBuffer(numBytes);

        for (byte[] response : responses) {
            baf.append(response,0,response.length);
        }

        return baf.toByteArray();
    }

    /**
     * Decodes a project
     * @param conn opened connection to database
     * @param _sdf date format to use
     * @param p project to enter into database
     * @return success or failure
     */
    private static int queryProject(Connection conn, SimpleDateFormat _sdf, Project p) {
        if (!BackEnd.createProject(conn, p.getName(), p.getTasks().size())) return FAILURE;

        for (Task t : p.getTasks()) {
            if (!BackEnd.insertTask(conn, p.getName(), t.getName(), _sdf.format(t.getStartTime()), _sdf.format(t.getEndTime()), t.getIP(), t.getPort()))
                return FAILURE; // Failed to insert task
        }
        return 0;
    }

    /**
     * Enters user into database for task
     * @param conn opened connection to database
     * @param take list of parameters provided to database for take
     * @return success or failure
     */
    private static int queryTake(Connection conn, Take take) {
        return BackEnd.setUser(conn, take.getProject(), take.getTask(), take.getPerson()) ? 0 : FAILURE;
    }

    /**
     * Gets list of projects to be encoded and sent back in response to get
     * projects query
     * @param conn opened connection to the database
     * @return list of projects (failure if empty)
     */
    private static Projects queryGetProjects(Connection conn) {
        return new Projects(BackEnd.getAllProjects(conn));
    }

    /**
     * Same as queryGetProjects except list returned is all of the projects created
     * @param conn opened connection to database
     * @return all projects in database in detail
     */
    private static ProjectsAnswer queryGetProjectsUnabridged(Connection conn) {
        ProjectsAnswer pa = new ProjectsAnswer();
        for (String projectName : BackEnd.getAllProjects(conn)) {
            pa.addProject(queryGetProject(conn, projectName));
        }
        return pa;
    }

    /**
     * Gets a project with its tasks from the database
     * @param conn already opened connection to database
     * @param projectName name of project to retrieve
     * @return project with its tasks
     */
    private static Project queryGetProject(Connection conn, String projectName) {
        Project p = new Project(projectName);
        for (String[] task : BackEnd.getTasks(conn, projectName)) {
            p.addTask(new Task(task[0], new Date(task[1]), new Date(task[2]), task[3], Integer.parseInt(task[4]), Boolean.parseBoolean(task[5])));
        }
        return p;
    }

}
