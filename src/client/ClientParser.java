package client;

import asn1objects.*;
import datatypes.Project;
import datatypes.ProjectOK;
import datatypes.Take;
import datatypes.Task;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;
import server.BackEnd;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.http.util.ByteArrayBuffer;
public class ClientParser {

    public static final int NUMTASKCOMPONENTS = 3;

    /**
     * Given input execute all commands
     * @param input string of commands (as many as won't break the JVM)
     * @return output response to parsing and executing commands
     * @throws SQLException if connection fails to close
     */
    public static synchronized byte[] parseClientInput(final String input, SimpleDateFormat sdf) throws IndexOutOfBoundsException, IOException, ParseException{

        int serverCommandBytes = 0;
        LinkedList<byte[]> serverCommands = new LinkedList<>();

        // Initialize commands structure and output string
        final String[] commands = input.split(";");

//        commands[commands.length - 1] = commands[commands.length - 1].replaceAll("\0", "");
        // if failure becomes true then loop ends and all input after index is printed
        // Loop until all commands in string are parsed
        int index = 0;
        while (index < commands.length) {
            // Given first command try to match it and if so execute it.
            String[] commandArg = commands[index].split(":");
            switch (commandArg[0]) {
                case "PROJECT_DEFINITION":

                    String name;

                    // Is there a name for the project?
                    name = commandArg[1];
                    Project p = new Project(name);

                    // int commandIndex = index;
                    final int numIndex = index + 1;
                    int tasksIndex = index + 2;
                    final int numTasks;

                    String[] nums = commands[numIndex].split(":");
                    numTasks = Integer.parseInt(nums[1]);

                    for (int i = 0; i < numTasks; i++) {
                        Task t = new Task(commands[tasksIndex], sdf.parse(commands[tasksIndex + 1]), sdf.parse(commands[tasksIndex + 2]), " ", 0, false);
                        p.addTask(t);
                        tasksIndex += 3;
                    }

                    index = tasksIndex;
                    byte[] encodedProject = new ASN1Project(p).getEncoder().getBytes();
                    serverCommands.add(encodedProject);
                    serverCommandBytes += encodedProject.length;
                    break;
                case "TAKE":
                    String[] user = commands[index + 1].split(":");
                    String[] project = commands[index + 2].split(":");

                    final String userArg = user[1];
                    final String projectArg = project[1];
                    final String task = commands[index + 3];

                    byte[] encodedTake = new ASN1Take(new Take(userArg, projectArg, task)).getEncoder().getBytes();

                    serverCommands.add(encodedTake);
                    serverCommandBytes += encodedTake.length;

                    index += 4;
                    break;
                case "GET_PROJECTS":

                    byte[] encodedGetProjects = new ASN1GetProjects().getEncoder().getBytes();
                    serverCommands.add(encodedGetProjects);
                    serverCommandBytes += encodedGetProjects.length;

                    break;
                case "GET_PROJECT":
                    final String projectName = commands[index + 1];
                    byte[] encodedGetProject = new ASN1GetProject().getEncoder().getBytes();
                    serverCommands.add(encodedGetProject);
                    serverCommandBytes += encodedGetProject.length;
                    break;
                default:
                    throw new IOException("Unrecognized command");
            }
        }

        ByteArrayBuffer baf = new ByteArrayBuffer(serverCommandBytes);

        for (byte[] b : serverCommands) {
            baf.append(b, 0, b.length);
        }

        return baf.toByteArray();
    }


    public static synchronized void printClientOutput(final Decoder dec) throws ASN1DecoderFail {
        ProjectOK pOK;
        StringBuilder sb = new StringBuilder();

        if (!dec.isEmptyContainer()) {
            pOK = new ASN1ProjectOK().decode(dec.getFirstObject(true));
        } else {
            throw new ASN1DecoderFail("No okays returned");
        }

        Iterator pOKIter = pOK.getOkays().iterator();
        while(!dec.isEmptyContainer()) {
            switch(dec.tagVal()) {
                case ASN1Project.TAGVALUE:
                    if (pOKIter.next() == 0) {
                        sb.append("OK;");
                    } else {
                        sb.append("FAIL;");
                    }

                    sb.append(new ASN1Project().decode(dec.getFirstObject(true)).toString());
                    break;
                case ASN1Projects.TAGVALUE:
                    if (pOKIter.next() == 0) {
                        sb.append("OK;");
                    } else {
                        sb.append("FAIL;");
                    }

                    sb.append(new ASN1Projects().decode(dec.getFirstObject(true)).toString());
                    break;
                case ASN1Take.TAGVALUE:
                    if (pOKIter.next() == 0) {
                        sb.append("OK;");
                    } else {
                        sb.append("FAIL;");
                    }

                    sb.append(new ASN1Take().decode(dec.getFirstObject(true)).toString());
                    break;
                default:
                    throw new ASN1DecoderFail("Unrecognized asn1 object");
            }
        }



    }

}