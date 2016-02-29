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

import java.net.Socket;
import java.net.ServerSocket;

import java.sql.SQLException;
import java.util.LinkedList;


public class Handler {
    private static LinkedList<String[]> setArgs() {
        LinkedList<String[]> arguments = new LinkedList<>();


        final String[] arg1 = {"p", "true", "specify port number"};
        final String[] arg2 = {"d", "true", "specify database location"};
        arguments.addLast(arg1);
        arguments.addLast(arg2);
        return arguments;

    }
    public static void main(final String [] args) {

        if (args.length < 1) {
            System.err.println("Usage: run.sh <port number>");
            System.exit(1);
        }


        Parser parseArgs = new Parser(setArgs());
        LogicEngine engine;
        CommandLine cmd;

        try {
            cmd = parseArgs.getCMD(args);
            //TODO:Make sure that any file works with the engine
            //TODO:If given abbreviated file name, get full file path
            engine = new LogicEngine(cmd.getOptionValue("d"));

            ServerSocket server = new ServerSocket(Integer.parseInt(cmd.getOptionValue("p")));

            for(;;) {
                Socket sock = server.accept();
                String IP = sock.getInetAddress().toString();
                int clientPort = sock.getPort();

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
                    System.out.println(line);
                    String output = engine.parseInput(line, IP, clientPort);
                    System.out.println(output);
                    writer.write("\n");
                    writer.write(output);
                    writer.write("\n");
                    writer.flush();
                }
                sock.close();

            }
        } catch (ParseException e) {
            System.err.println("Illegal argument entered");
            e.printStackTrace();
            System.exit(-1);
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Problem connecting to socket");
            e.printStackTrace();
        }  catch (SQLException e) {
            System.err.println("Could not access database. Does the program have write rights to the directory?");
            System.exit(-1);
        }

        /*try {
            cmd = parseArgs.getCMD(args);
            engine = new LogicEngine(cmd.);
            port = new ServerSocket(Integer.parseInt(cmd.getOptionValue("p")));

            client = port.accept();
            reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));

            while (true) {

                String[] input = reader.readLine().split(";");
                String output = engine.response(input);
            }

        } catch (ParseException e) {
            System.out.println("Illegal argument format");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Illegal port number");
            e.printStackTrace();
        } */


    }


}
