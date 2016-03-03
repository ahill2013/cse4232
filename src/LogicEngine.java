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

/**
 * Parses single line of input from the user.
 */
public class LogicEngine {
    BackEnd be;
    private Connection conn;
    private String dbFile;

    /**
     * Checks whether proposed database location exists and if a database does not
     * already exist with the given name then creates a database
     * @param dbLocation proposed location
     * @throws SQLException if creating/opening database fails
     */
    public LogicEngine(String dbLocation) throws SQLException {
        dbFile = dbLocation;
        be = new BackEnd(dbFile);
    }

    /**
     * Given input execute all commands
     * @param input string of commands (as many as won't break the JVM)
     * @param IP IP of origin for the commands
     * @param port which port they were sent from on client's computer
     * @return output response to parsing and executing commands
     * @throws SQLException if connection fails to close
     */
    public String parseInput(String input, String IP, int port) throws SQLException {

        // Open connection. If connection fails then kill the program. Any errors here are unforeseen
        try {
            be = new BackEnd(dbFile);
            conn = be.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Initialize commands structure and output string
        StringBuilder output = new StringBuilder();
        String[] commands = input.split(";");

        // if failure becomes true then loop ends and all input after index is printed
        // Loop until all commands in string are parsed
        boolean _failure = false;
        int index = 0;
        while (index < commands.length && !_failure) {
            // Given first command try to match it and if so execute it.
            String[] commandArg = commands[index].split(":");
            switch (commandArg[0]) {
                case "PROJECT_DEFINITION":
                    String name = null;

                    // Is there a name for the project?
                    try {
                        name = commandArg[1];
                    } catch (IndexOutOfBoundsException e) {
                        _failure = true;
                        break;
                    }

                    int commandIndex = index;
                    int numIndex = index + 1;
                    int tasksIndex = index + 2;
                    int numTasks = 0;

                    // If there is a project definition and nothing after it then go to else and break
                    if (numIndex < commands.length) {
                        String[] nums = commands[numIndex].split(":");
                        // If the name for the next command is wrong break
                        if (!nums[0].equals("TASKS") || !(nums.length == 2)) {
                            _failure = true;
                            break;
                        }

                        // If the number of tasks is not a valid number break
                        try {
                            numTasks = Integer.parseInt(commands[numIndex].split(":")[1]);
                        } catch (Exception e) {
                            _failure = true;
                            break;
                        }
                    } else {
                        _failure = true;
                        break;
                    }

                    // If there aren't enough names and times for the number of tasks break
                    if ((tasksIndex + 3*numTasks) > commands.length) {
                        _failure = true;
                        break;
                    }

                    //create project with name and tasks and implicit task table if number of task is greater than zero
                    boolean projectCreated = be.createProject(conn, commandArg[1], numTasks);
                    boolean taskCreated = true;

                    // If project is successfully created add tasks
                    if (projectCreated) {

                        for (int i = 0; i < numTasks; i++) {
                            taskCreated = be.insertTask(conn, name, commands[tasksIndex], commands[tasksIndex + 1], commands[tasksIndex + 2], IP, port);
                            tasksIndex += 3;
                            if (!taskCreated) {
                                break;
                            }
                        }
                    }

                    // If project and task creation is successful print output else fail and break
                    if (projectCreated && taskCreated) {
                        appendOutput(output, "OK");
                        projectOutput(output, commands, index, tasksIndex);
                    } else {
                        _failure = true;
                        break; // Not strictly necessary but makes more sense here
                    }

                    index = tasksIndex;
                    break;
                case "TAKE":
                    // If there aren't enough strings to fill commands
                    if ((index + 3) > commands.length) {
                        _failure = true;
                        break;
                    } else {
                        String[] user = commands[index + 1].split(":");
                        String[] project = commands[index + 2].split(":");

                        // If format of strings is not correct output failure and break
                        if (user.length != 2 || project.length !=2 ||
                                !user[0].equals("USER") || !project[0].equals("PROJECT")) {
                            _failure = true;
                            break;
                        }

                        String userArg = user[1];
                        String projectArg = project[1];
                        String task = commands[index + 3];
                        // If user is set execute output otherwise fail and break
                        if (be.setUser(conn, projectArg, task, userArg)) {
                            appendOutput(output, "OK");
                            appendOutput(output, commands[index + 1]);
                            appendOutput(output, commands[index + 2]);
                            appendOutput(output, commands[index + 3]);
                        } else {
                            _failure = true;
                            break;
                        }
                    }

                    index += 4;
                    break;
                case "GET_PROJECTS":
                    // Get all projects and append to output unless database is locked or does not exist. If locked print failure
                    try {
                        LinkedList<String> projects = be.getAllProjects(conn);
                        appendOutput(output, "OK");
                        appendOutput(output, "PROJECTS:" + projects.size());

                        if (projects.size() != 0) {
                            for (String project : projects) {
                                appendOutput(output, project);
                            }
                        }
                        index += 1;
                    } catch (SQLException e) {
                        _failure = true;
                    }
                    break;
                case "GET_PROJECT":
                    // If there is not project name, break
                    if (!((index + 1) < commands.length)) {
                        _failure = true;
                    } else {
                        String project = commands[index + 1];
                        // Get the number of tasks for the project. If zero then just print out project.
                        // If less than one then the project does not exist or there is a database problem.
                        // Otherwise execute retrieval
                        int numberTasks = be.getNumberTasks(conn, project);
                        if (numberTasks == 0) {
                            appendOutput(output, "OK");
                            appendOutput(output, "PROJECT_DEFINITION:" + project);
                            index += 2;
                        } else if (numberTasks < 0) {
                            _failure = true;
                        } else {
                            LinkedList<String[]> tasks = be.getTasks(conn,project);
                            // Check whether all projects are done or waiting and whether their status needs
                            // to be changed
                            if (checkStatus(project, tasks)) {
                                tasks = be.getTasks(conn, project);
                                appendOutput(output, "OK");
                                appendOutput(output, "PROJECT_DEFINITION:" + project);
                                appendOutput(output, "TASKS:" + tasks.size());
                                // For each task output all of the information on the task. Either was set of output
                                // if waiting and another if done.
                                for (String[] part : tasks) {
                                    // Prevent error if owner has not been set
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
                                        appendOutput(output, part[0]); //Name
                                        // Start is not output if task is complete
                                        appendOutput(output, part[2]); // End
                                        appendOutput(output, part[3]); // Owner
                                        appendOutput(output, part[5]); // IP
                                        appendOutput(output, part[6]); // Port
                                        appendOutput(output, "Done");  // Status
                                    }
                                }
                                index+=2;
                            }
                        }
                    }
                    break;
                default:
                    // If not a recognizable command output failure
                    _failure = true;
            }
        }

        if (_failure) {
            failureFormat(output, commands, commands.length, index);
        }

        output.append("\n");
        be.closeConnection(conn);
        return output.toString();
    }

    /**
     * When a project is created successfully append to the output string using this specific method.
     * Made to handle the large number of tasks likely to be included
     *
     * @param output string builder
     * @param commands list of commands
     * @param index index to begin from
     * @param tasksIndex the index of the end of the tasks
     */
    private void projectOutput(StringBuilder output, String[] commands, int index, int tasksIndex) {
        for (int i = index; i < tasksIndex; i++) {
            appendOutput(output, commands[i]);
        }
    }

    /**
     * For whenever a failure occurs in parsing data. Prints fail for all remaining commands after encountering
     * a bad command in input. This outputs all remaining data from the input after the failure, but first prepending
     * the word "Fail" to the output.
     * @param output string builder
     * @param commands list of commands
     * @param commandsLength total number of commands
     * @param index index to begin failure formatting from
     */
    private static void failureFormat(StringBuilder output, String[] commands, int commandsLength, int index) {
        appendOutput(output, "Fail");
        for (int i = index; i < commandsLength; i++) {
            appendOutput(output, commands[i]);
        }
    }

    /**
     * Given a project, determine whether the status of all of its tasks are appropriate. If not change the
     * status to reflect the tasks state
     * @param project project name
     * @param tasks the number of tasks to read
     * @return whether all of the status's were successfully checked. Will return false if a table is locked or corrupted
     */
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

    /**
     * Checks whether a task is past its completion point
     * @param end string representing the time of completion
     * @return compareTo() output after string has been formatted for simple date format
     */
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

    /**
     * Close the connection to the database
     */
    public void closeLogicEngine() {
        try {
            be.closeConnection(conn);

        } catch (SQLException e) {
            System.err.println("ERROR: Can't close connection in LogicEngine");
        }
    }

    /**
     * Given an output StringBuilder, append the specified string to the StringBuilder
     *
     * @param output an already created string builder
     * @param append string to append
     */
    static void appendOutput(StringBuilder output, String append) {
        if (output.length() > 0) {
            output.append(";");
        }
        output.append(append);
    }

//    /**
//     * For debugging specifically
//     */
//    public void printDatabase() {
//        be.printAllProjects(conn);
//        be.printAllTables(conn);
//    }
}
