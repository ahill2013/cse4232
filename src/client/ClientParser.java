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

package client;

import asn1objects.*;

import datatypes.*;

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;

import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class ClientParser {

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
                    index += 1;
                    break;
                case "GET_PROJECT":
                    final String projectName = commands[index + 1];
                    ASN1GetProject asn1gp = new ASN1GetProject(new GetProject(projectName));
                    byte[] encodedGetProject = asn1gp.getEncoder().getBytes();
                    serverCommands.add(encodedGetProject);
                    serverCommandBytes += encodedGetProject.length;
                    index += 2;
                    break;
                case "REGISTER":
                    final String enterProject = commands[index + 1];
                    ASN1Enter asn1e = new ASN1Enter(new EnterLeave(enterProject, true));
                    byte[] encodedEnter = asn1e.getEncoder().getBytes();
                    serverCommands.add(encodedEnter);
                    serverCommandBytes += encodedEnter.length;
                    index += 2;
                    break;
                case "LEAVE":
                    final String leaveProject = commands[index + 1];
                    ASN1Leave asn1l = new ASN1Leave(new EnterLeave(leaveProject, true));
                    byte[] encodedLeave = asn1l.getEncoder().getBytes();
                    serverCommands.add(encodedLeave);
                    serverCommandBytes += encodedLeave.length;
                    index += 2;
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


    /**
     * Parse a decoder and print the parsed decoder to the standard output
     * @param dec received packet from server
     * @throws ASN1DecoderFail if packet is not well formed (should not occur)
     */
    public static synchronized void printClientOutput(final Decoder dec) throws ASN1DecoderFail {
        StringBuilder sb = new StringBuilder();


        while(!dec.isEmptyContainer()) {

//            ProjectOK pOK = new ASN1ProjectOK().decode(dec.getFirstObject(true));
//            if (pOK.getOkays() == 0) {
//                sb.append("OK;");
//            } else {
//                sb.append("FAIL;");
//            }

            switch(dec.tagVal()) {
                case ASN1ProjectOK.TAGVALUE:
                    ProjectOK pOK = new ASN1ProjectOK().decode(dec.getFirstObject(true));
                    if (pOK.getOkays() == 0) {
                        sb.append("OK;");
                    } else {
                        sb.append("FAIL;");
                    }
                    break;
                case ASN1Project.TAGVALUE:
                    sb.append(new ASN1Project().decode(dec.getFirstObject(true)).toString());
                    break;
                case ASN1Projects.TAGVALUE:
                    sb.append(new ASN1Projects().decode(dec.getFirstObject(true)).toString());
                    break;
                case ASN1Take.TAGVALUE:
                    sb.append(new ASN1Take().decode(dec.getFirstObject(true)).toString());
                    break;
                case ASN1Enter.TAGVALUE:
                    sb.append(new ASN1Enter().decode(dec.getFirstObject(true)).toString());
                    break;
                case ASN1Leave.TAGVALUE:
                    sb.append(new ASN1Leave().decode(dec.getFirstObject(true)).toString());
                    break;
                default:
                    throw new ASN1DecoderFail("Unrecognized asn1 object");
            }
        }
        System.out.println(sb);
    }

}