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

import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class Client {

    public static synchronized void main (final String args[]) throws UnknownHostException, ParseException {

        String m_args[] = args;

        OptParser parseArgs = new OptParser();
        CommandLine cmd;
        Thread cl = null;

        try {
            cmd = parseArgs.getCMD(m_args);
            String nonOptsArgs[] = new String[2];
            int count = 0;
            while (cmd.getArgs().length > 0) {
                nonOptsArgs[count] = cmd.getArgs()[0];
                String newArgs[] = new String[m_args.length-1];
                int counter = 0;
                for (String arg: m_args) {
                    if (!nonOptsArgs[count].equals(arg)) {
                        newArgs[counter] = arg;
                        counter++;
                    }
                }
                m_args = newArgs;
                count++;
                cmd = parseArgs.getCMD(newArgs);
            }
            // NOTE: nonOptsArgs[1] may be null. I am assuming if -d was not specified then it is no
            //       longer null, and an IP and port were given. If -d was specified then I assume
            //       that there is no IP and the port is in nonOptsArgs[0]
            String IP = null;
            int port;
            if (cmd.hasOption("d")) {
                port = Integer.parseInt(nonOptsArgs[0]);
            } else {
                if (nonOptsArgs[0].equals("localhost")) nonOptsArgs[0] = "127.0.0.1";
                if (nonOptsArgs[1].equals("localhost")) nonOptsArgs[1] = "127.0.0.1";
                if (nonOptsArgs[0].contains(".")) {
                    IP = nonOptsArgs[0];
                    port = Integer.parseInt(nonOptsArgs[1]);
                } else {
                    IP = nonOptsArgs[1];
                    port = Integer.parseInt(nonOptsArgs[0]);
                }
            }

            // display help menu
            if (cmd.hasOption("h")) {
                OptParser.printHelpMenu();
                System.exit(0);
            }

            // handle conflicting flags
            if (cmd.hasOption("t") && cmd.hasOption("u")) {
                System.err.println("Cannot use both 't' and 'u' flags at the same time.");
                System.err.println("Choose only one.");
                System.exit(0);
            }

            DatagramSocket sock = null;
            if (cmd.hasOption("u")) {
                try {
                    sock = new DatagramSocket();
                } catch (SocketException e) {
                    System.out.println("Could not open UDP socket on local host");
                }
                cl = new Thread(new ClientListener(sock));
                cl.start();
            }
            Scanner stdin = new Scanner(System.in);

            while (true) {
                // read user input
                String input = stdin.nextLine();

                // Get out of program
                if (input.equals("EXIT")) {
                    break;
                }

                byte[] serverInput;
                try {
                    serverInput = ClientParser.parseClientInput(input, new SimpleDateFormat("yyyy-MM-dd:hh'h'mm'm'ss's'SSS'Z'"));


                    if (cmd.hasOption("d")) {
                        if (cmd.hasOption("u")) sendCommandUDP(sock, serverInput, cmd.getOptionValue("d"), port);
                        else sendCommandTCP(serverInput, cmd.getOptionValue("d"), port);
                    } else {
                        if (cmd.hasOption("u")) sendCommandUDP(sock, serverInput, IP, port);
                        else sendCommandTCP(serverInput, IP, port);
                    }

                } catch (Exception e) {
                    System.out.println("Poor Command entered");
                    System.out.println(e.getMessage());
                    continue;
                }

                System.out.println();

            }
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
        } finally {
            if (cl != null) {
                cl.interrupt();
            }
        }

    }

    private static void sendCommandUDP(DatagramSocket sock, final byte[] input, final String host, final int _port) throws UnknownHostException {
        if (sock == null) {
            System.err.println("Trying to send UDP command in TCP mode");
            System.exit(-1);
        }
        final InetAddress inetAddress = InetAddress.getByName(host);
        try {
//            byte[] buffer = new byte[4*1024];
            sock.getPort();
            DatagramPacket send = new DatagramPacket(input, input.length, inetAddress, _port);
            sock.send(send);

//            DatagramPacket receipt = new DatagramPacket(buffer, buffer.length);
//            sock.receive(receipt);
//
//            Decoder rec = new Decoder(receipt.getData(), 0, receipt.getLength());
//            ClientParser.printClientOutput(rec);
        } catch (SocketException e) {
            System.out.println("Could not connect to remote host");
        } catch (IOException e) {
            System.out.println("Network error");
            e.printStackTrace();
        }
//        } catch (ASN1DecoderFail e) {
//            System.out.println("Could not decode asn1");
//        }
    }

    private static synchronized void sendCommandTCP(final byte[] input, final String host, final int port) {

        byte[] ASNresponse = new byte[32768];
        try {
            Socket sock = new Socket(host, port);
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

}
