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

import org.apache.commons.cli.*;
import org.tmatesoft.sqljet.core.*;
import org.tmatesoft.sqljet.browser.*;
import org.antlr.runtime.*;

import java.io.IOException;
import java.io.FileNotFoundException;

//import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.Socket;
import java.net.ServerSocket;

import java.nio.charset.Charset;
import java.util.LinkedList;


public class Handler {
    private static LinkedList<String[]> setArgs() {
        LinkedList<String[]> arguments = new LinkedList<>();


        final String[] arg1 = {"p", "true", "specify port number"};
        final String[] arg2 = {"d", "true", "specify database location"};
        arguments.addLast(arg1);
        arguments.addLast(arg2);
        return arguments;

    }
    public static void main(final String [] args) {

        if (args.length < 1) {
            System.err.println("Usage: run.sh <port number>");
            System.exit(1);
        }


        Parser parseArgs = new Parser(setArgs());
        LogicEngine engine;
        CommandLine cmd;

        try {
            cmd = parseArgs.getCMD(args);
            engine = new LogicEngine(cmd.getOptionValue("d"));
        } catch (ParseException e) {
            System.err.println(cmd.getOptions());
            e.printStackTrace();
        }


        ServerSocket port;
        Socket client;
        BufferedReader reader;
        BufferedWriter writer;

        try {
            cmd = parseArgs.getCMD(args);
            engine = new LogicEngine(cmd.)
            port = new ServerSocket(Integer.parseInt(cmd.getOptionValue("p")));

            client = port.accept();
            reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));

            while (true) {

                String[] input = reader.readLine().split(";");
                String output = engine.response(input);
            }

        } catch (ParseException e) {
            System.out.println("Illegal argument format");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Illegal port number");
            e.printStackTrace();
        }

        //OutputStream out;
        /*String input;
        BufferedReader reader;
        BufferedWriter out;

        try {
            ServerSocket serverSock = new ServerSocket(portNum);

            while (true) {
                Socket clientSock = serverSock.accept();
                reader =  new BufferedReader(new InputStreamReader(clientSock.getInputStream(), “latin1”));
                out = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream(), “latin1”));
                out.write("Hello!\n"); out.flush();
                for(int k=0;k<3;k++) {
                    if((input=reader.readLine())==null) break;
                    out.write(input+"\n");
                    out.flush();
                }
                clientSock.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            System.out.println("IOExeption caught listening to port " + portNum);
            System.out.println(e.getMessage());
        }*/
    }


}

// Reference
// https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoServer.java