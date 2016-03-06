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

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Brings up TCP socket connection and reads commands until ended. Program can be closed by using
 * the command EXIT or QUIT. Accepts a single connection at a time. If a connection is closed, then
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

        try {
            cmd = parseArgs.getCMD(finalArgs);
            //TODO:Make sure that any file works with the engine
            //TODO:If given abbreviated file name, get full file path
            engine = new LogicEngine(cmd.getOptionValue("d"));

            ServerSocket server = new ServerSocket(Integer.parseInt(cmd.getOptionValue("p")));

            System.out.println("Waiting for connection from client...\n");

            for(;;) {
                Socket sock = server.accept();   // accept connection
                // Get the IP address the client connected from.
                // We use substring() because toString() returns "HOSTNAME/physicalIP".
                // We have no use for the hostname in our case, so we take the physical IP address only
                String IP = sock.getInetAddress().toString().substring(sock.getInetAddress().toString().indexOf('/') + 1);
                int clientPort = sock.getPort(); // get the port the client is connected to

                System.out.println("Connected: " + IP);

                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

                // Greet User upon connection to server
                writer.write("Hello User! You may now enter a command.\n\n");
                writer.flush();

                for (;;) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.equals("")) { // if no input then do nothing and wait for more
                        writer.flush();
                    } else {
                        //System.out.println(line);
                        String output = engine.parseInput(line, IP, clientPort);
                        //System.out.println(output);
                        writer.write("\n");
                        writer.write(output);
                        writer.write("\n");
                        writer.flush();
                    }
                }
                sock.close();

            }
        } catch (ParseException e) {
            System.err.println("Illegal argument entered");
            e.printStackTrace();
            System.exit(-1);
        } catch (BindException e) {
            System.err.println("Port already in use. Specify a different port.");
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Problem connecting to socket");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Could not access database. Does the program have write rights to the directory?");
            System.exit(-1);
        }

    }

}
