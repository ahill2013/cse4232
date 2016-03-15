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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Handles receiving and replying to all UDP packets sent to server. Receives a maximum buffer of
 * 2^15 bytes; however, such size is not recommended because the reply may be significantly longer and cause
 * the UDP Handler to raise an exception.
 *
 * Implements Runnable with methods run() and terminate()
 *
 * Opened as a thread which forces graceful shutdown
 */
public class UDPHandler implements Runnable {

    private static final int BUFFER_SIZE = 32768;
    /**
     * _port is the port that the handler must listen to
     * _running is a flag to track whether program is being terminated
     * engine is an input parser inputs containing a connection instance for the database
     */
    private int _port;
    private boolean _running = true;
    private LogicEngine engine;

    /**
     * Creates instance of UDP Handler, saves the port the handler listens to, and attempts to start a connection
     * to the database.
     *
     * @param port is the port the server is listening on
     * @param dbFile is the location of the already created database
     */
    public UDPHandler(int port, String dbFile) {
        _port = port;
        engine = new LogicEngine(dbFile);
    }

    /**
     * Opens a UDP DatagramSocket listening on the predefined port and handling all UDP interactions.
     * The handler takes a packet, reinterprets it into a String for the instance of LogicEngine, and then replies
     * to the client. The next packet is then loaded until the shutdown hook is activated at which point the thread
     * terminates
     */
    @Override
    public synchronized void run() {
        try {
            DatagramSocket socket = new DatagramSocket(_port);
            DatagramPacket receive = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    terminate();
                }
            });

            while (_running) {
                try {
                    // Receive
                    socket.receive(receive);
                    InetAddress packet_address = receive.getAddress();
                    int packet_port = receive.getPort();

                    // Interpret
                    byte[] reply = engine.parseInput(new String(receive.getData()).replaceAll("\n", "").replaceAll("\0", ""),
                            packet_address.toString().substring(packet_address.toString().indexOf("/") + 1), packet_port).getBytes();

                    // Reply
                    DatagramPacket send = new DatagramPacket(reply, reply.length, receive.getAddress(), receive.getPort());
                    socket.send(send);

                    // Reset the receive buffer for the next buffer (prevents overflow and over-read)
                    receive.setData(new byte[BUFFER_SIZE]);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("Could not open database for UDP packet");
                }
            }
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            engine.closeLogicEngine();
        }
    }

    /**
     * Tell the program to stop running
     */
    public void terminate() {
        _running = false;
    }
}
