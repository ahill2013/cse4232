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

import java.net.BindException;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Listens for all client connections to the server and then starts a TCPHandler to handle each client connection.
 *
 */
public class TCPThreadedServer implements Runnable {

    private String _dbFile;
    private int _port;
    private boolean _running = true;


    public TCPThreadedServer(int port, String dbFile) {
        _port = port;
        _dbFile = dbFile;
    }


    @Override
    public synchronized void run() {
        try {
            ServerSocket tcpServer = new ServerSocket(_port);

            Runtime.getRuntime().addShutdownHook(new Thread() {
              @Override
              public void run() { terminate(); }
            });
            //System.out.println("Waiting for connection from client...\n");
            while (_running) {
                final Thread tcpHandler = new Thread(new TCPHandler(tcpServer.accept(), _dbFile));
                tcpHandler.start();
            }
        } catch (BindException e) {
            System.err.println("Port already in use. Please specify a different port.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Problem connecting to socket");
            e.printStackTrace();
        }
    }

    public void terminate() {
        _running = false;
    }
}
