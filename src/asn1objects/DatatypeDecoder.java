package asn1objects;

import datatypes.*;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import server.BackEnd;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by armin1215 on 4/5/16.
 */
public class DatatypeDecoder {

    public static final int FAILURE = -1;

    public static byte[] serverQuery(String _dbfile, SimpleDateFormat sdf, Decoder dec) throws ASN1DecoderFail, SQLException {
        Connection conn = BackEnd.openConnection(_dbfile);
        ProjectOK projectOK = new ProjectOK();

        LinkedList<Integer> okays = new LinkedList<>();

        while (!dec.isEmptyContainer()) {
            int ok = 0;
            switch (dec.tagVal()) {
                case ASN1Project.TAGVALUE:
                    Project input = new ASN1Project().decode(dec.getFirstObject(true));
                    ok = queryProject(conn, sdf, input);
                    projectOK.addOkay(ok);

                    // Procedure for failure to get the project
                    if (ok != 0) {
                        System.out.print("FAIL;");
                    }
                    System.out.println(input);

                    break;
                case ASN1Take.TAGVALUE:
                    Take tk = new ASN1Take().decode(dec.getFirstObject(true));
                    ok = queryTake(conn, tk);

                    if (ok != 0) {
                        System.out.print("Fail;");
                    }
                    System.out.println(tk);

                    break;
                case ASN1GetProjects.TAGVALUE:
                    Projects ps = queryGetProjects(conn);

                    if (ps.getProjectNames().isEmpty()) {
                        System.out.println("FAIL;");
                        ok = FAILURE;
                    }
                    System.out.println(ps);

                    break;
                case ASN1GetProjectsUnabridged.TAGVALUE:
                    ProjectsAnswer pa = queryGetProjectsUnabridged(conn);

                    if (pa.getProjects().isEmpty()) {
                        System.out.println("FAIL;");
                        ok = FAILURE;
                    }
                    System.out.println(pa);

                    break;
                case ASN1GetProject.TAGVALUE:
                    GetProject gp = new ASN1GetProject().decode(dec.getFirstObject(true));
                    Project output = queryGetProject(conn, gp.getName());

                    if (output.getTasks().isEmpty()) {
                        System.out.println("FAIL;");
                        ok = FAILURE;
                    }
                    System.out.println(gp);

                    break;
                default:
                    throw new ASN1DecoderFail("Invalid ASN1 Tag Value");
            }
            okays.add(ok);
        }

        return new ASN1ProjectOK(okays).getEncoder().getBytes();
    }

    private static String clientDecoder(Decoder dec) throws ASN1DecoderFail {




    }
    private static int queryProject(Connection conn, SimpleDateFormat _sdf, Project p) {
        if (!BackEnd.createProject(conn, p.getName(), p.getTasks().size())) return FAILURE;

        for (Task t : p.getTasks()) {
            if (!BackEnd.insertTask(conn, p.getName(), t.getName(), _sdf.format(t.getStartTime()), _sdf.format(t.getEndTime()), t.getIP(), t.getPort()))
                return FAILURE; // Failed to insert task
        }
        return 0;
    }

    private static int queryTake(Connection conn, Take take) {
        return BackEnd.setUser(conn, take.getProject(), take.getTask(), take.getPerson()) ? 0 : FAILURE;
    }

    private static Projects queryGetProjects(Connection conn) {
        return new Projects(BackEnd.getAllProjects(conn));
    }

    private static ProjectsAnswer queryGetProjectsUnabridged(Connection conn) {
        ProjectsAnswer pa = new ProjectsAnswer();
        for (String projectName : BackEnd.getAllProjects(conn)) {
            pa.addProject(queryGetProject(conn, projectName));
        }
        return pa;
    }


    private static Project queryGetProject(Connection conn, String projectName) {
        Project p = new Project(projectName);
        for (String[] task : BackEnd.getTasks(conn, projectName)) {
            p.addTask(new Task(task[0], new Date(task[1]), new Date(task[2]), task[3], Integer.parseInt(task[4]), Boolean.parseBoolean(task[5])));
        }
        return p;
    }

}
