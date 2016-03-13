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

import java.io.*;
import java.net.BindException;
import java.net.Socket;
import java.sql.SQLException;

public class TCPHandler implements Runnable {

    private Socket sock;
    private LogicEngine engine;

    private boolean _running = true;

    public TCPHandler(Socket sock, String dbFile) {
        this.sock = sock;
        engine = new LogicEngine(dbFile);
    }

    @Override
    public void run() {

        try {
            // Get the IP address the client connected from.
            // We use substring() because toString() returns "HOSTNAME/physicalIP".
            // We have no use for the hostname in our case, so we take the physical IP address only
            final String IP = sock.getInetAddress().toString().substring(sock.getInetAddress().toString().indexOf('/') + 1);
            final int clientPort = sock.getPort(); // get the port the client is connected to

            System.out.println("Connected: " + IP);

            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            // Greet User upon connection to server
            writer.write("Hello User! You may now enter a command.\n\n");
            writer.flush();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    engine.closeLogicEngine();
                }
            });

            while (_running) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Could not access database. Does the program have write rights to the directory?");
        } finally {
            engine.closeLogicEngine(); //TODO:not sure about this
        }
    }

    public void terminate() {
        _running = false;
        engine.closeLogicEngine();
    }
}
