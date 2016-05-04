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

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Listens for UDP packets arriving at the socket at any time after the socket is opened
 *
 * Prints their output to standard out.
 */
public class ClientListener implements Runnable {
    private DatagramSocket _sock;
    private boolean _running = true;

    public ClientListener(DatagramSocket sock) {
        _sock = sock;
    }
    @Override
    public void run() {
        try {
            /**
             * Shut down the thread by calling terminate to set _running to false
             */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    terminate();
                }

            });

            while (_running) {
                if (Thread.interrupted()) terminate();
                byte[] buffer = new byte[4*1024];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

                _sock.receive(dp);
                // Will print bad output
                ClientParser.printClientOutput(new Decoder(dp.getData(), 0, dp.getLength()));
            }
        } catch (SocketException e) {
            // Not expected exceptions
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASN1DecoderFail asn1DecoderFail) {
            // Anything sent from the server must be well formed
            asn1DecoderFail.printStackTrace();
        }
    }

    public void terminate() {
        _running = false;
    }
}
