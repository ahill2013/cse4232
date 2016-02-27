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

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

public class LogicEngine {
    private BackEnd be;
    private Connection conn;
    private File dbFile;

    public LogicEngine(String dbLocation) throws FileNotFoundException {

        try {
            be = new BackEnd(dbLocation);
            conn = be.openConnection();

        } catch (SQLException e) {
            System.err.println("ERROR: SQL Exception in LogicEngine.java");
        }

    }

    public String parseInput(String input, String IP, int port) {

        String[] splitInput = input.split(";");
        String[] commandArg = splitInput[0].split(":");
        switch (commandArg[0]) {
            case "PROJECT_DEFINITION":
                String[] numtaskArg = splitInput[1].split(":");
                be.createProject(conn, commandArg[1], Integer.parseInt(numtaskArg[1]));
                int inputCount = 3;
                for (int i=0; i<Integer.parseInt(numtaskArg[1]); i++)
                    //be.insertTask(conn, commandArg[1], splitInput[inputCount++], splitInput[inputCount++], splitInput[inputCount++]);
                break;
            case "TAKE":
                String[] userArg = splitInput[1].split(":");
                String[] projectArg = splitInput[2].split(":");
                // TODO Implement function for taking tasks in BackEnd
                //be.takeTask(conn, userArg[1], projectArg[1], splitInput[3]);
                break;
            case "GET_PROJECTS":
                be.printAllProjects(conn);
                break;
            case "GET_PROJECT":
                //be.getTasks(commandArg[1]);
                break;
            default:
                System.err.println("ERROR: Invalid Command");
        }

        return null;
    }

    public void closeLogicEngine() {
        try {
            be.closeConnection(conn);

        } catch (SQLException e) {
            System.err.println("ERROR: Can't close connection in LogicEngine");
        }
    }

}
