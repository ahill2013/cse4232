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

package server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Handles one client's interaction with the server by using the LogicEngine to open a
 * new connection to the database and continue parsing input from the client until the client ends the interaction
 * or the program is concluded.
 *
 * Uses Runnable with the methods run() and terminate()
 *
 * Started by normal method tcpHandlerInstance.start() and has a shutdown hook built in to force graceful termination.
 */
public class TCPHandler implements Runnable {

    /**
     * Socket to receive input from (socket received from creator)
     * engine is the input parser and contains the database interaction
     * _running is a flag set to false when the thread is asked to finish
     */
    private Socket sock;
    private LogicEngine engine;
    private boolean _running = true;

    /**
     * Sets the client socket connection and gives the location of the database for the LogicEngine to use
     * @param sock already created connection to client
     * @param dbFile location of already created database
     */
    public TCPHandler(Socket sock, String dbFile) {
        this.sock = sock;
        engine = new LogicEngine(dbFile);
    }

    /**
     * Parses input from the client connection until the connection is closed. Replies to each set of commands
     * with a buffered reader and writer
     */
    @Override
    public synchronized void run() {

        try {
            // Get the IP address the client connected from.
            // We use substring() because toString() returns "HOSTNAME/physicalIP".
            // We have no use for the hostname in our case, so we take the physical IP address only
            final String IP = sock.getInetAddress().toString().substring(sock.getInetAddress().toString().indexOf('/') + 1);
            final int clientPort = sock.getPort(); // get the port the client is connected to

            System.out.println("TCP Connection: " + IP);

            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            // Greet User upon connection to server
            writer.write("Hello User! You may now enter a command.\n\n");
            writer.flush();

            // Force graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    terminate();
                }
            });

            // Read the input until the client closes the connection or until the server is closed
            while (_running) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.equals("EXIT") || line.equals("QUIT")) {
                    break;
                }
                if (line.equals("")) { // if no input then do nothing and wait for more
                    writer.flush();
                } else {
                    String output = engine.parseInput(line, IP, clientPort);
                    writer.write("\n");
                    writer.write(output);
                    writer.write("\n");
                    writer.flush();
                }
            }

            sock.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found for the database");
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Could not access database. Does the program have write rights to the directory?");
        } finally {
            engine.closeLogicEngine();
        }
    }

    /**
     * Called on shutdown to tell the handler to stop reading input from the client and close the connection
     */
    public void terminate() {
        _running = false;
    }
}
