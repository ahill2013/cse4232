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

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

//import asn1.net.ddp2p.ASN1.*;
import asn1objects.ASN1Project;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import datatypes.Project;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

public class Client {
    static int BUFFER = 32500;
    static int _port;
    private boolean _running = false;


    public static synchronized void main (final String args[]) throws UnknownHostException, ParseException {

        String flag = "-t";  // use tcp by default
        String IP = "localhost";
        int port = 2132;

        // long complicated argument handling.  Should probably replace with a more efficient way eventually
        if (args.length == 2) {
            if (args[0].contains(".")) {
                IP = args[0];
                port = Integer.parseInt(args[1]);
            } else {
                IP = args[1];
                port = Integer.parseInt(args[0]);
            }
        }
        if (args.length == 3) {
            if (args[0].contains(".")) {
                IP = args[0];
                if (args[1].equals("-t") || args[1].equals("-u")) {
                    if (args[1].equals("-u"))
                        flag = "-u";
                    port = Integer.parseInt(args[2]);
                } else {
                    port = Integer.parseInt(args[1]);
                    if (args[2].equals("-u"))
                        flag = "-u";
                }

            } else if (args[1].contains(".")) {
                IP = args[1];
                if (args[0].equals("-t") || args[0].equals("-u")) {
                    if (args[0].equals("-u"))
                        flag = "-u";
                    port = Integer.parseInt(args[2]);
                } else {
                    port = Integer.parseInt(args[0]);
                    if (args[2].equals("-u"))
                        flag = "-u";
                }

            } else {
                IP = args[2];
                if (args[0].equals("-t") || args[0].equals("-u")) {
                    if (args[0].equals("-u"))
                        flag = "-u";
                    port = Integer.parseInt(args[1]);
                } else {
                    port = Integer.parseInt(args[0]);
                    if (args[1].equals("-u"))
                        flag = "-u";
                }
            }
        }

        Scanner stdin = new Scanner(System.in);

        while (true) {

            String input = stdin.nextLine();

            // Get out of program
            if (input.equals("EXIT")) {
                break;
            }


            byte[] serverInput;
            try {
                serverInput = ClientParser.parseClientInput(input, new SimpleDateFormat("yyyy-MM-dd:hh'h'mm'm'ss's'SSS'Z'"));

                if (flag.equals("-t")) {
                    sendCommandTCP(serverInput, IP, port);
                } else if (flag.equals("-u")) {
                    sendCommandUDP(serverInput, IP, port);
                }

            } catch (Exception e) {
                System.out.println("Poor Command entered");
                System.out.println(e.getMessage());
                continue;
            }

            System.out.println();

        }


    }

    private static void sendCommandUDP(final byte[] input, String IP, int _port) throws UnknownHostException {

        final InetAddress inetAddress = InetAddress.getByName(IP);
        DatagramSocket sock;
        try {
            byte[] buffer = new byte[4*1024];
            sock = new DatagramSocket();
            DatagramPacket send = new DatagramPacket(input, input.length, inetAddress, _port);
            sock.send(send);

            DatagramPacket receipt = new DatagramPacket(buffer, buffer.length);
            sock.receive(receipt);

            Decoder rec = new Decoder(receipt.getData(), 0, receipt.getLength());
            ClientParser.printClientOutput(rec);
        } catch (SocketException e) {
            System.out.println("Could not connect to remote host");
        } catch (IOException e) {
            System.out.println("Network error");
            e.printStackTrace();
        } catch (ASN1DecoderFail e) {
            System.out.println("Could not decode asn1");
        }
    }

    private static void receiveUDP() throws UnknownHostException {
        DatagramSocket sock;
        byte[] buffer = new byte[BUFFER];
        try {
            sock = new DatagramSocket();

            while (true) {
                DatagramPacket receipt = new DatagramPacket(buffer, buffer.length);
                sock.receive(receipt);
                ClientParser.printClientOutput(new Decoder(receipt.getData()));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASN1DecoderFail e) {
            e.printStackTrace();
        }
    }

    private static synchronized void sendCommandTCP(final byte[] input, final String IP, final int port) {

        byte[] ASNresponse = new byte[32768];
        try {
            Socket sock = new Socket(IP, port);
            InputStream reader = sock.getInputStream();
            OutputStream writer = sock.getOutputStream();

            writer.write(input);
            writer.flush();
            int outputSize = reader.read(ASNresponse);
            Decoder output = new Decoder(ASNresponse, 0, outputSize);
            ClientParser.printClientOutput(output);

            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASN1DecoderFail asn1DecoderFail) {
            asn1DecoderFail.printStackTrace();
        }
    }

/*
    /**
     * This method sends the command to shutdown the server over UDP, and close this program
     *
     * @throws UnknownHostException if the specified inetAddress cannot be found on the machine
     *//*
    private void sendExitUDP() throws UnknownHostException {
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
    */
}
