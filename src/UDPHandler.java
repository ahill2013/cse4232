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
import java.net.SocketException;

public class UDPHandler implements Runnable {

    DatagramSocket sock;
    DatagramPacket packet;

    public UDPHandler(int port) throws SocketException {
        sock = new DatagramSocket(port);
    }

    @Override
    public void run() {

        byte[] buffer = new byte[65508];
        packet = new DatagramPacket(buffer, buffer.length);

        for (;;) {
            try {
                sock.receive(packet);
                //doStuffWithData(packet, buffer)?
                System.out.println(buffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
