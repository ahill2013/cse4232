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

package client;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPClient {

    static int _port;

    public static void main (final String args[]) {

        _port = Integer.parseInt(args[0]);

        // The script being run will set args[1] to either a 1 or 2
        if (args[1].equals("1")) {
            try {
                final String[] commandList = {
                        "GET_PROJECTS",
                        "PROJECT_DEFINITION:Exam2;TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2014-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2014-03-15:18h30m00s001Z",
                        "TAKE;USER:Johny;PROJECT:Exam2;Buy paper",
                        "TAKE;USER:Mary;PROJECT:Exam2;Write exam",
                        "GET_PROJECTS",
                        "GET_PROJECT;Exam2",
                        "EXIT" };

                for (String command : commandList) {
                    System.out.println(command);
                    System.out.println();
                    sendCommand(command);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else if (args[1].equals("2")) {
            try {
                final String[] commandList = {
                        "PROJECT_DEFINITION:Exam;TASKS:2;Buy paper;2000-03-12:18h30m00s001Z;2000-03-15:18h30m00s001Z;Write exam;2020-03-15:18h30m00s001Z;2020-04-15:18h30m00s001Z",
                        "TAKE;USER:Johny;PROJECT:Exam;Buy paper",
                        "TAKE;USER:Mary;PROJECT:Exam;Write exam",
                        "GET_PROJECT;Exam",
                        "EXIT" };

                for (String command : commandList) {
                    System.out.println(command);
                    System.out.println();
                    sendCommand(command);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends UDP packets to the server. Each packet may contain multiple commands.
     *
     * @param command - string being sent to the server for processing
     * @throws UnknownHostException if the specified inetAddress cannot be found on the machine
     */
    private static void sendCommand(final String command) throws UnknownHostException {

        if (command.equals("EXIT")) {
            sendExit();
            System.exit(0);
        }

        final byte[] input = command.getBytes();
        final byte[] buffer = new byte[32768];

        final InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        DatagramSocket sock;
        try {
            sock = new DatagramSocket();
            sock.send(new DatagramPacket(input, input.length, inetAddress, _port));
            DatagramPacket receipt = new DatagramPacket(buffer, buffer.length);
            sock.receive(receipt);
            System.out.println(new String(receipt.getData(), StandardCharsets.UTF_8) );
            System.out.flush();
            receipt.setData(new byte[buffer.length]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends the command to shutdown the server over UDP, and close this program
     *
     * @throws UnknownHostException if the specified inetAddress cannot be found on the machine
     */
    private static void sendExit() throws UnknownHostException {
        final byte[] command = ("EXIT").getBytes();

        final InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        DatagramSocket sock;
        try {
            sock = new DatagramSocket();
            sock.send(new DatagramPacket(command, command.length, inetAddress, _port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
