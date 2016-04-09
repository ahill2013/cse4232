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

        // TODO add better argument handling
        String IP = args[0];
        int port = Integer.parseInt(args[1]);
        String flag = "-t";  // use tcp by default
        flag = args[2];

        Scanner stdin = new Scanner(System.in);
//        Thread t = null;
//        if (flag.equals("-u")) {
//            t = new Thread
//
//        }
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

    private static synchronized String sendCommandTCP(final byte[] input, final String IP, final int port) {

        byte[] ASNresponse = null;
        Project response = null;
        try {
            Socket sock = new Socket(IP, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            writer.write(input.toString());
            writer.flush();
            ASNresponse = reader.readLine().getBytes();
            response = new ASN1Project().decode(new Decoder(ASNresponse));

            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASN1DecoderFail asn1DecoderFail) {
            asn1DecoderFail.printStackTrace();
        }

        return response.toString();
    }

    /*
    /**
     * Sends UDP packets to the server. Each packet may contain multiple commands.
     *
     * @param command - string being sent to the server for processing
     * @throws UnknownHostException if the specified inetAddress cannot be found on the machine
     *//*
    private static void sendCommandUDP(final String command) throws UnknownHostException {

        if (command.equals("EXIT")) {
            sendExitUDP();
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
*/
    /**
     * This method sends the command to shutdown the server over UDP, and close this program
     *
     * @throws UnknownHostException if the specified inetAddress cannot be found on the machine
     */
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
}
