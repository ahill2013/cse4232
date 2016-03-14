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

public class UDPHandler implements Runnable {

    private int _port;
    private boolean _running = true;
    private LogicEngine engine;

    public UDPHandler(int port, String dbFile) {
        _port = port;
        engine = new LogicEngine(dbFile);
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[32768];
            DatagramSocket socket = new DatagramSocket(_port);
            DatagramPacket receive = new DatagramPacket(buffer, buffer.length);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    engine.closeLogicEngine();
                }
            });

            while (_running) {
                try {

                    socket.receive(receive);
                    InetAddress packet_address = receive.getAddress();
                    int packet_port = receive.getPort();

                    byte[] reply = engine.parseInput(new String(receive.getData()).replaceAll("\n",
                            "").replaceAll("\0", ""), packet_address.toString(), packet_port).getBytes();

                    DatagramPacket send = new DatagramPacket(reply, reply.length, receive.getAddress(), receive.getPort());
                    socket.send(send);

                    System.out.println(new String(buffer, StandardCharsets.UTF_8));
                    receive.setData(new byte[buffer.length]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("Could not open database for UDP packet");
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            engine.closeLogicEngine();
        }
    }

    public void stop() {
        _running = false;
        engine.closeLogicEngine();
    }
}
