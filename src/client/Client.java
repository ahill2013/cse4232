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
import java.util.Scanner;

//import asn1.net.ddp2p.ASN1.*;
import asn1objects.ASN1Project;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import datatypes.Project;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;

public class Client {

    static int _port;

    public static void main (final String args[]) throws UnknownHostException, ParseException {

        // TODO add better argument handling
        String IP = args[0];
        int port = Integer.parseInt(args[1]);
        String flag = "-t";  // use tcp by default
        flag = args[2];

        Scanner stdin = new Scanner(System.in);

        for (;;) {
            System.out.println("Which command would you like to send?");
            System.out.println("\t1. PROJECT_DEFINITION");
            System.out.println("\t2. TAKE");
            System.out.println("\t3. GET_PROJECTS");
            System.out.println("\t4. GET_PROJECT");
            System.out.println("\t5. EXIT");
            System.out.print("Choose a number from the above list: ");
            int input = stdin.nextInt();

            while (input > 5 || input < 1) {
                System.out.print("Invalid input. Enter a valid number (1-5): ");
                input = stdin.nextInt();
            }

            System.out.println();

            String command;
            switch (input) {
                case 1:
                    System.out.println("Finish the command");
                    System.out.print("PROJECT_DEFINITION:");
                    command = "PROJECT_DEFINITION:" + stdin.next();
                    //sendCommandUDP(encodeProject(command));
                    break;
                case 2:
                    System.out.println("Finish the command");
                    System.out.print("TAKE;");
                    command = "TAKE;" + stdin.next();
                    //sendCommandUDP(encodeTake(command));
                    break;
                case 3:
                    System.out.println("Sending command");
                    command = "GET_PROJECTS";
                    //sendCommandUDP(encodeTake(command));
                    break;
                case 4:
                    System.out.println("Finish the command");
                    System.out.print("GET_PROJECT;");
                    command = "GET_PROJECT;" + stdin.next();
                    //sendCommandUDP(encodeProject(command));
                    break;
                case 5:
                    System.out.println("Sending command");
                    command = "EXIT";
                    //sendCommandUDP(encodeTake(command));
                    break;
                default:
                    System.err.println("Invalid command");
                    command = null;
            }

            if (flag.equals("-t")) {
                sendCommandTCP(command.getBytes(), IP, port);
            } else if (flag.equals("-u")) {
                sendCommandUDP(command.getBytes(), IP, port);
            }
        }

        /*
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd:hh'h'mm'm'ss's'SSS'Z'");
        _port = Integer.parseInt(args[0]);

        Task t, t1;
        List<Task> tasks = new LinkedList<Task>();

        t = new Task("TEST", _sdf.parse("1980-01-01:01h01m01s001Z"), _sdf.parse("1980-01-01:01h01m01s001Z"), "localhost", 2235, false);
        t1 = new Task("test", _sdf.parse("1980-01-01:01h01m01s001Z"), _sdf.parse("1980-01-01:01h01m01s001Z"), "localhost", 2235, false);
        tasks.add(t);
        tasks.add(t1);


        Project p = new Project("Test", tasks);
        Encoder enc = new ASN1Project(p).getEncoder();
        Encoder encTask = new ASN1Project(p).getEncoder();

        Decoder decTask = new Decoder(encTask.getBytes());
        Decoder dec = new Decoder(enc.getBytes());

        System.out.println(dec.getTypeByte());
        System.out.println(decTask.getTypeByte());


//        ASN1Task test = new ASN1Task(t);
//
//        Encoder enc = test.getEncoder();
        sendCommandUDP(enc.getBytes());
        */
    }

    private static void sendCommandUDP(final byte[] input, final String IP, final int port) throws UnknownHostException {

        DatagramSocket sock;
        try {
            byte[] buffer = new byte[4*1024];
            sock = new DatagramSocket();
            sock.send(new DatagramPacket(input, input.length, InetAddress.getByName(IP), _port));
            DatagramPacket receipt = new DatagramPacket(buffer, buffer.length);
            sock.receive(receipt);
            Project response = new ASN1Project().decode(new Decoder(buffer));

            System.out.println(response.toString());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASN1DecoderFail e) {
            e.printStackTrace();
        }
    }

    private static String sendCommandTCP(final byte[] input, final String IP, final int port) {

        byte[] ASNresponse = null;
        Project response = null;
        try {
            Socket sock = new Socket(IP, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            writer.write(input.toString());
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
    private static void sendExitUDP() throws UnknownHostException {
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
