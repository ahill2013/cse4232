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

package server.UDP;

import asn1objects.ASN1Project;
import asn1objects.ASN1Task;
import datatypes.Project;
import datatypes.Task;

import java.util.LinkedList;
import java.util.TimerTask;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * A task to be reported at a specific time. Each instance is added to a Timer.
 */
public class SendTracked extends TimerTask implements Runnable {
    private Project _p;
    private Task _t;
    public SendTracked(String project, Task t) {
        _p = new Project(project);
        _p.addTask(t);
        _t = t;
    }

    /**
     * Send a UDP packet to the client containing the task
     */
    @Override
    public void run() {
        try {
            byte[] buffer;
            DatagramSocket receiver = new DatagramSocket();
            buffer = new ASN1Project(_p).getEncoder().getBytes();
            DatagramPacket send = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(_t.getIP()), _t.getPort());
            receiver.send(send);
        } catch (SocketException e) {
            System.err.println("Failed to send task");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
