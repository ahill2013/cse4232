/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2016
                Author:  wnyffenegger2013@my.fit.edu
                Author:  ahill2013@my.fit.edu
                Florida Tech, Computer Science

       This program is free software; you can redistribute it and/or modify
       it under the terms of the GNU Affero General Public License as published by
       the Free Software Foundation; either the current version of the License, or
       (at your option) any later version.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.

      You should have received a copy of the GNU Affero General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */
/* ------------------------------------------------------------------------- */

package server.UDP;

import asn1objects.*;
import datatypes.*;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import org.apache.http.util.ByteArrayBuffer;
import server.Database.BackEnd;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Static class with one public method to deal with a single request from
 * a UDP or TCP handler to the server.
 *
 * serverQuery is only public method and handles all of this
 */
public class UDPDecoder {

    public static final int FAILURE = -1;
    private LinkedList<UDPEventTracker> _udpet;

    private static final long timetoupdate = 300000;
    private long lastupdate;

    public boolean _running = true;

    public UDPDecoder() {
        lastupdate = System.currentTimeMillis();
        _udpet = new LinkedList<>();
    }

    /**
     * Takes a database location, desired date format (not controlled by user)
     * and a Decoder and then replies with a byte array containing the outcomes of
     * each successfully decoded server query as a series of successes or failures.
     * After the outcomes, comes either straightforward replies to the request or a
     * reiteration of the request.
     *
     * Records all of the registered and leave commands sent by clients as well as a record of those requests.
     * It cancels those requests on command and schedules them.
     *
     * @param _dbfile location of the database on the local system
     * @param sdf format for dates entered into the database
     * @param dec already created decoder
     * @return response to all client requests
     * @throws SQLException if the database is locked or the location of the data base does
     * not exist
     */
    public byte[] serverQuery(String _dbfile, SimpleDateFormat sdf, Decoder dec, InetAddress packet_address, int port) throws SQLException, ParseException {

        String ipAddress = packet_address.toString().substring(packet_address.toString().indexOf('/') + 1);
        LinkedList<Integer> okays = new LinkedList<>();

        int numBytes = 0;
        LinkedList<byte[]> responses = new LinkedList<>();
        Connection conn = null;
        try {
            conn = BackEnd.openConnection(_dbfile);
            ProjectOK projectOK = new ProjectOK();

            while (!dec.isEmptyContainer()) {
                int ok = 0;
                byte[] response;
                switch (dec.tagVal()) {
                    case ASN1Project.TAGVALUE:
                        Project input = new ASN1Project().decode(dec.getFirstObject(true));
                        input.addPort(port);
                        input.addIP(ipAddress);
                        ok = queryProject(conn, sdf, input);
                        projectOK.addOkay(ok);

                        // Procedure for failure to get the project
                        if (ok != 0) {
                            System.out.print("FAIL;");
                            System.out.println(input);
                            response = new ASN1Project(input).getEncoder().getBytes();
                        } else {
                            Project output = queryGetProject(conn, sdf, input.getName());
                            response = new ASN1Project(output).getEncoder().getBytes();

                            System.out.println(queryGetProject(conn, sdf, input.getName()));
                        }

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
                        String check = new ASN1GetProjects().decode(dec.getFirstObject(true));
                        Projects ps = queryGetProjects(conn);
                        response = new ASN1Projects(ps).getEncoder().getBytes();

                        if (ps.getProjectNames().isEmpty()) {
                            System.out.println("FAIL;");
                            ok = FAILURE;
                        }
                        System.out.println(ps);

                        break;
                    case ASN1GetProjectsUnabridged.TAGVALUE:
                        ProjectsAnswer pa = queryGetProjectsUnabridged(conn, sdf);
                        response = new ASN1ProjectsAnswer(pa).getEncoder().getBytes();

                        if (pa.getProjects().isEmpty()) {
                            System.out.println("FAIL;");
                            ok = FAILURE;
                        }
                        System.out.println(pa);

                        break;
                    case ASN1GetProject.TAGVALUE:
                        GetProject gp = new ASN1GetProject().decode(dec.getFirstObject(true));
                        Project output = queryGetProject(conn, sdf, gp.getName());
                        response = new ASN1Project(output).getEncoder().getBytes();

                        if (output.getTasks().isEmpty()) {
                            System.out.println("FAIL;");
                            ok = FAILURE;
                        }
                        System.out.println(gp);
                        System.out.println(output);
                        break;
                    case ASN1Enter.TAGVALUE:
                        EnterLeave enter = new ASN1Enter().decode(dec.getFirstObject(true));
                        ok = executeEnter(conn, sdf, enter, packet_address, port);

                        if (ok == FAILURE) {
                            System.out.println("FAIL;");
                        }
                        System.out.println(enter);
                        response = new ASN1Enter(enter).getEncoder().getBytes();
                        break;
                    case ASN1Leave.TAGVALUE:
                        EnterLeave leave = new ASN1Leave().decode(dec.getFirstObject(true));
                        ok = executeLeave(leave, packet_address, port);

                        if (ok == FAILURE) {
                            System.out.println("FAIL;");
                        }
                        System.out.println(leave);
                        response = new ASN1Leave(leave).getEncoder().getBytes();
                        break;
                    default:
                        throw new ASN1DecoderFail("Invalid ASN1 Tag Value");
                }

//                if (ok == 0) {
//                    byte[] okay = new ASN1ProjectOK(0).getEncoder().getBytes();
//                    responses.add(okay);
//                    numBytes += okay.length;
//                }
                byte[] okay = new ASN1ProjectOK(ok).getEncoder().getBytes();
                responses.add(okay);
                numBytes += okay.length;
                responses.add(response);
                numBytes += response.length;
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
    private static ProjectsAnswer queryGetProjectsUnabridged(Connection conn, SimpleDateFormat sdf) throws ParseException {
        ProjectsAnswer pa = new ProjectsAnswer();
        for (String projectName : BackEnd.getAllProjects(conn)) {
            pa.addProject(queryGetProject(conn, sdf, projectName));
        }
        return pa;
    }

    /**
     * Gets a project with its tasks from the database
     * @param conn already opened connection to database
     * @param projectName name of project to retrieve
     * @return project with its tasks
     */
    private static Project queryGetProject(Connection conn, SimpleDateFormat sdf, String projectName) throws ParseException {
        Project p = new Project(projectName);
        for (String[] task : BackEnd.getTasks(conn, projectName)) {
            String taskName = task[0];
            Date start = sdf.parse(task[1]);
            Date end = sdf.parse(task[2]);
            String user = task[3];
            boolean status = Boolean.parseBoolean(task[4]);
            String ip = task[5];
            int port = Integer.parseInt(task[6]);

            if (!status && isDone(end) >= 0) {
                BackEnd.setStatus(conn, projectName, taskName, 1);
                status = true;
            }

            Task t = new Task(taskName, start, end, ip, port, status);
            p.addTask(t);
        }
        return p;
    }

    /**
     * Schedule all tasks finishing in an hour from every project named in an enter-leave object
     * to be sent to the respective client from which the enter leave object originated.
     *
     * @param conn already opened connection to a database
     * @param sdf desired simple date format
     * @param el enter leave object
     * @param ip ip that object was sent from
     * @param port port that object was sent from
     * @return 0 for successfully scheduled all tasks occurring in next hour to occur
     */
    private int executeEnter(Connection conn, SimpleDateFormat sdf, EnterLeave el, InetAddress ip, int port) {
        UDPEventTracker etracker = new UDPEventTracker(ip, port);
        try {
            checkStatusTrackers();
            int added = 0;
            for (String project : el.getProjects()) {
                added += etracker.addAllTasks(project, queryGetProject(conn, sdf, project).getTasks());
            }

            // If tasks were added then add the tracker to the current list of running trackers
            if (added > 0) {
                _udpet.add(etracker);
            }
        } catch (ParseException e) {
            return FAILURE;
        }
        return 0;
    }

    /**
     * Deregister a client from listening to scheduled tasks by cancelling all scheduled reports for the client
     * concerning that particular project.
     * @param el enter leave object with list of projects to stop listening to
     * @param ip remote host of the client
     * @param port remote port number
     * @return 0 for successful execution
     */
    private int executeLeave(EnterLeave el, InetAddress ip, int port) {
        Iterator<UDPEventTracker> trackers = _udpet.iterator();
        while (trackers.hasNext()) {
            UDPEventTracker tracker = trackers.next();
            if (tracker.equals(ip, port)) {
                tracker.remove(el.getProjects());

                trackers.remove();
                return 0;
            }
        }
        return FAILURE;
    }

    /**
     * Is the task done yet?
     * @param end the final time of the task
     * @return whether it is before, after, or during the task's execution
     */
    private static int isDone(final Date end) {
        try {
            final Date current = new Date();
            return current.compareTo(end);
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    /**
     * If the list of tracked projects has not been updated in the last 10 minutes then go through and remove
     * all expired tracked projects and update all projects for the tasks completed.
     */
    private void checkStatusTrackers() {
        long x = System.currentTimeMillis() - lastupdate;
        if (timetoupdate < x) {
            Iterator<UDPEventTracker> eventTracker = _udpet.iterator();
            while (eventTracker.hasNext()) {
                UDPEventTracker tracker = eventTracker.next();
                tracker.update();
                if (tracker.size() == 0) {
                    eventTracker.remove();
                }
            }
        }
    }

}
