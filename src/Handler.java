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

import org.apache.commons.cli.*;

import java.io.*;

import java.net.BindException;
import java.net.Socket;
import java.net.ServerSocket;

//import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Brings up TCP socket connection and reads commands until ended. Program can be closed by using
 * the commands EXIT or QUIT. Accepts a single connection at a time. If a connection is closed, then
 * the server waits for a new client to open another connection.
 *
 *     Main class that runs the server
 */
public class Handler {

    /**
     * Returns the expected format to be input into CLI as the options used to bring the server online
     * @return returns the arguments
     */
    private static LinkedList<String[]> setArgs() {

        LinkedList<String[]> arguments = new LinkedList<>();
        final String[] arg1 = {"p", "true", "specify port number"};
        final String[] arg2 = {"d", "true", "specify database location"};
        arguments.addLast(arg1);
        arguments.addLast(arg2);

        return arguments;
    }

    /**
     * Brings the server online and then begins reading and writing to clients.
     * @param args command line arguments -p port# -d databasefile.db
     */
    public static void main(final String [] args) {

        // The following is here in case the program is not started with run.sh, or a test script
        // It finds a relative path and does some error checking

        // Attempting to find correct path. This should get the directory from which the program and
        // JVM was started.
        // NOTE:  This should not be the "Current working directory"
        //String currentRelativePath = Paths.get("").toAbsolutePath().toString();

        String [] finalArgs = new String[0];
        if (args.length == 2) {
            if (args[0].equals("-p")) {
                System.out.println("Using port: " + args[1]);
                finalArgs = new String[]{args[0], args[1], "-d", "temp.db"};
            }
            else if (args[0].equals("-d")) {
                System.out.println("Using default port: 2132");
                finalArgs = new String[]{"-p", "2132", args[0], args[1]};
            }
        } else {
            finalArgs = new String[args.length];
            for (int i=0; i<finalArgs.length; i++)
                finalArgs[i] = args[i];
        }

        Parser parseArgs = new Parser(setArgs());
        LogicEngine engine;
        CommandLine cmd;

        final int maxConnections = -1;
        int currentConnections = 0;

        try {

            cmd = parseArgs.getCMD(finalArgs);
            try {
                BackEnd.openDatabase(cmd.getOptionValue("d"));
            } catch (SQLException e) {
                System.err.println("Invalid location for database or corrupted database");
            }
            engine = new LogicEngine(cmd.getOptionValue("d"));

            int port = Integer.parseInt(cmd.getOptionValue("p"));
            //ServerSocket server = new ServerSocket(port);

            // System.out.println("Waiting for connection from client...\n");

            final Thread tcpServer = new Thread(new TCPThreadedServer(port, cmd.getOptionValue("d")));
            final Thread udpServer = new Thread(new UDPHandler(port, cmd.getOptionValue("d")));
            tcpServer.start();
            udpServer.start();
            try {
                tcpServer.join();
                udpServer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // udpServer.join();

        } catch (ParseException e) {
            System.err.println("Illegal argument entered");
            e.printStackTrace();
            System.exit(-1);
        }
//        } catch (BindException e) {
//            System.err.println("Port already in use. Specify a different port.");
//        } catch (FileNotFoundException e) {
//            System.err.println("File not found");
//            e.printStackTrace();
//            System.exit(-1);
//        } catch (IOException e) {
//            System.out.println("Problem connecting to socket");
//            e.printStackTrace();
//        }
        //} catch (InterruptedException e) {
        //    e.printStackTrace();

    }

}
