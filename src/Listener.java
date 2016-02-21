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


public class Listener {
    public enum CODES {START, EXIT, RETRIEVE, RECORD}

    public Listener() {}

    public static void main(final String [] args) {

        if (args.length != 1) {
            System.err.println("Usage: run.sh <port number>");
            System.exit(1);
        }

        int portNum = Integer.parseInt(args[0]);

        //OutputStream out;
        String input;
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
                    if((input=reader.readLine()==null) break;
                    out.write(input+"\n");   out.flush();}
                    clientSock.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            System.out.println("IOExeption caught listening to port " + portNum);
            System.out.println(e.getMessage());
        }
    }


}

// Reference
// https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoServer.java