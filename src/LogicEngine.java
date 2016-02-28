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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class LogicEngine {
    BackEnd be;
    private Connection conn;
    private String dbFile;

    public LogicEngine(String dbLocation) throws SQLException {
        dbFile = dbLocation;

    }

    public String parseInput(String input, String IP, int port) throws SQLException {
        try {
            be = new BackEnd(dbFile);
            conn = be.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        StringBuilder output = new StringBuilder();
        String[] commands = input.split(";");


        boolean _failure = false;
        int index = 0;
        while (index < commands.length && !_failure) {
            String[] commandArg = commands[index].split(":");
            switch (commandArg[0]) {
                case "PROJECT_DEFINITION":

                    String name = commandArg[1];
                    // Get the number of tasks
                    int commandIndex = index;
                    int numIndex = index + 1;
                    int tasksIndex = index + 2;
                    int numTasks = 0;

                    // If there is a project definition and nothing after it
                    if (numIndex < commands.length) {
                        try {
                            numTasks = Integer.parseInt(commands[numIndex].split(":")[1]);
                        } catch (Exception e) {
                            failureFormat(output, commands, commands.length, index);
                            _failure = true;
                        }
                    } else {
                        failureFormat(output, commands, commands.length, index);
                        _failure = true;
                        break;
                    }

                    if ((tasksIndex + 3*numTasks) > commands.length) {
                        failureFormat(output, commands, commands.length, index);
                        _failure = true;
                        break;
                    }

                    //create project with name and tasks and implicit task table if number of task is greater than zero
                    boolean projectCreated = be.createProject(conn, commandArg[1], numTasks);
                    boolean taskCreated = true;

                    if (projectCreated) {

                        for (int i = 0; i < numTasks; i++) {
                            taskCreated = be.insertTask(conn, name, commands[tasksIndex], commands[tasksIndex + 1], commands[tasksIndex + 2], IP, port);
                            tasksIndex += 3;
                            if (!taskCreated) {
                                break;
                            }
                        }
                    }

                    if (projectCreated && taskCreated) {
                        appendOutput(output, "OK");
                        projectOutput(output, commands, index, tasksIndex);
                    } else {
                        failureFormat(output, commands, commands.length, index);
                        _failure = true;
                    }

                    index = tasksIndex;
                    break;
                case "TAKE":

                    if ((index + 3) > commands.length) {
                        appendOutput(output, "Fail");
                        for (int i = index; i < commands.length; i++) {
                            appendOutput(output, commands[index]);
                        }
                        _failure = true;
                    } else {
                        String userArg = commands[index + 1].split(":")[1];
                        String projectArg = commands[index + 2].split(":")[1];
                        String task = commands[index + 3];
                        if (be.setUser(conn, projectArg, task, userArg)) {
                            appendOutput(output, "OK");
                            appendOutput(output, commands[index + 1]);
                            appendOutput(output, commands[index + 2]);
                            appendOutput(output, commands[index + 3]);
                        } else {
                            failureFormat(output, commands, commands.length, index);
                            _failure = true;
                        }
                    }

                    index += 4;
                    break;
                case "GET_PROJECTS":
                    LinkedList<String> projects = be.getAllProjects(conn);

                    if (projects.peekFirst().equals("Failure")) {
                        failureFormat(output, commands, commands.length, index);
                        _failure = true;
                    } else {
                        appendOutput(output, "OK");
                        appendOutput(output, "PROJECTS:" + projects.size());
                        for (String project : projects) {
                            appendOutput(output, project);
                        }
                    }
                    index += 1;
                    break;
                case "GET_PROJECT":
                    if (!((index + 1) < commands.length)) {
                        appendOutput(output, "Fail");
                        appendOutput(output, commands[index]);
                        _failure = true;
                    } else {
                        String project = commands[index + 1];
                        LinkedList<String[]> tasks = be.getTasks(conn,project);
                        if (be.getNumberTasks(conn, project) == 0) {
                            appendOutput(output, "OK");
                            appendOutput(output, "PROJECT_DEFINITION:" + project);
                            index += 2;
                        } else if (tasks.peek()[0].equals("Failure")) {
                            failureFormat(output, commands, commands.length, index);
                            _failure = true;
                        } else {
                            if (checkStatus(project, tasks)) {
                                tasks = be.getTasks(conn, project);
                                appendOutput(output, "OK");
                                appendOutput(output, "PROJECT_DEFINITION:" + project);
                                appendOutput(output, "TASKS:" + tasks.size());

                                for (String[] part : tasks) {

                                    if (part[3] == null) {
                                        part[3] = "";
                                    }
                                    if (Integer.parseInt(part[4]) == 0) {
                                        appendOutput(output, part[0]); // Name
                                        appendOutput(output, part[1]); // Start
                                        appendOutput(output, part[2]); // End
                                        appendOutput(output, part[3]); // Owner
                                        appendOutput(output, part[5]); // IP
                                        appendOutput(output, part[6]); // Port
                                        appendOutput(output, "Waiting"); // Status
                                    } else {
                                        appendOutput(output, part[0]);
                                        // Start is not output if task is complete
                                        appendOutput(output, part[2]);
                                        appendOutput(output, part[3]);
                                        appendOutput(output, part[5]);
                                        appendOutput(output, part[6]);
                                        appendOutput(output, "Done");
                                    }
                                }
                                index+=2;
                            } else {
                                failureFormat(output, commands, commands.length, index);
                                _failure = true;
                            }
                        }
                    }
                    break;
                default:
                    failureFormat(output, commands, commands.length, index);
                    _failure = true;
            }
        }
        output.append("\n");
        be.closeConnection(conn);
        return output.toString();
    }

    //Do not use for anyone else
    private void projectOutput(StringBuilder output, String[] commands, int index, int tasksIndex) {
        for (int i = index; i < tasksIndex; i++) {
            appendOutput(output, commands[i]);
        }
    }

    private void failureFormat(StringBuilder output, String[] commands, int commandsLength, int index) {
        appendOutput(output, "Fail");
        for (int i = index; i < commandsLength; i++) {
            appendOutput(output, commands[i]);
        }
    }

    private boolean checkStatus(String project, LinkedList<String[]> tasks) {
        for (String[] part : tasks) {
            if (Integer.parseInt(part[4]) == 0) {
                int done = isDone(part[2]);
                if (done == 1) {
                    be.setStatus(conn, project, part[0], done);
                } else if (done == Integer.MAX_VALUE) {
                    return false;
                }
            }
        }
        return true;
    }

    private int isDone(String end) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss-SSS'Z'");
            String update = end.replace('m', '-');
            update = update.replace('s', '-');
            update = update.replace('h', '-');
            Date endTime = dateFormat.parse(update);
            Date current = new Date();
            return current.compareTo(endTime);
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MAX_VALUE;
        }
    }

    public void closeLogicEngine() {
        try {
            be.closeConnection(conn);

        } catch (SQLException e) {
            System.err.println("ERROR: Can't close connection in LogicEngine");
        }
    }

    public static void appendOutput(StringBuilder output, String append) {
        if (output.length() > 0) {
            output.append(";");
        }
        output.append(append);
    }

    public void printDatabase() {
        be.printAllProjects(conn);
        be.printAllTables(conn);
    }
}
