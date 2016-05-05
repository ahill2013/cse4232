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

package server;

/**************************************************************
* Important methods
 * createProject
 * insertTask this and create project have to be done together to put tasks in
 * setUser set the owner of a task
 * setStatus set flag to 0 for waiting and 1 for done
 * getTasks
 * getProjects
 * closeConnection end a connection
 ****************************************************************/

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Class for doing all back end management required to run the server. Contains methods for opening connections
 * to databases, adding projects, and adding tasks. It also contains methods for the retrieval of information from the
 * database.
 *
 *      The intended use of BackEnd is to support LogicEngine and nothing else.
 *
 *      A list of important methods in the class includes:
 *      createProject, insertTask, setUser, setStatus, getTasks, getProjects,openConnection, closeConnection
 */
public class BackEnd {

    /**
     * Hard coded PROJECTS_LIST which lists the project names and the number of tasks associated with them.
     * With a hardcoded task list size used for counting columns for output
     * The String file is in case the connection is closed and someone wishes to reopen the connection.
     */
    private static final String PROJECTS = "PROJECTS_LIST";
//    private static final int TASKLISTSIZE = 7;

//    /**
//     * Creates database if not already created with projects_list. Each instance is ready to handle queries and updates
//     * for the database that is input into its system.
//     *
//     * @param dbPath the relative path to the file. May error if program does not have write permissions to location.
//     * @throws SQLException error caused when database is locked, does not exist, or directory does not exist
//     * or permissions are not given
//     */
//    public BackEnd(String dbPath) throws SQLException {
//        openDatabase(dbPath);
//    }

    /**
     * Creates a project by inserting it into the list of projects in the database and creating a table for all of the
     * tasks in the project. If project is already created, it replaces the project completely.
     *
     * Note: erases project completely if it already exists so there is an implicit assumption that project names are
     * unique. Prepending with times may be wisest solution to prevent erasing.
     *
     * @param conn already opened connection to an sqlite database
     * @param projectName name of the project wished to be created
     * @param tasks number of tasks in the project
     * @return whether project was created successfully
     */
    public static boolean createProject(Connection conn, String projectName, int tasks) {
        try {
            String addProjectName = "REPLACE INTO PROJECTS_LIST(NAME, TASKS) VALUES('" + projectName + "'," + tasks + ")";
            Statement create = conn.createStatement();
            create.executeUpdate(addProjectName);
            if (tasks > 0) {
                String taskTable = getTaskTable(projectName);
                String enteredTable =getEnteredTable(projectName);

                String dropTable = "DROP TABLE IF EXISTS '" + taskTable + "'";
                String dropEntered = "DROP TABLE IF EXISTS '" + enteredTable +"'";
                create.execute(dropTable);
                create.execute(dropEntered);
                conn.commit();
                final String createTable = "CREATE TABLE '" + taskTable +
                        "'(NAME TEXT NOT NULL, START TEXT NOT NULL, END TEXT NOT NULL, OWNER TEXT," +
                        " STATUS INT NOT NULL, IP TEXT NOT NULL, PORT INT NOT NULL)";
                create.execute(createTable);
                final String createEntered = "CREATE TABLE '" + enteredTable + "' (NAME TEXT NOT NULL)";
                create.execute(createEntered);
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Failed to create project");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Go to the PROJECTS_LIST table and return all of the projects as a list of strings (names)
     * @param conn already opened connection to an sqlite database
     * @return string of names. Returns "Failure" as first and only string in list if it fails to read from the database
     * This is weird but also surprisingly useful.
     */
    public static LinkedList<String> getAllProjects(Connection conn) {
        LinkedList<String> projects = new LinkedList<>();

        try {
            Statement create = conn.createStatement();
            ResultSet resultSet = create.executeQuery("SELECT * FROM " + PROJECTS);
            conn.commit();

            while (resultSet.next()) {
                projects.addLast(resultSet.getString("NAME"));
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve all project names");
            e.printStackTrace();
        }

        return projects;
    }


//    /**
//     * For use later
//     * @param conn
//     * @param projectName
//     */
//    public void removeProject(Connection conn, String projectName) {
//        removeTasks(conn, getTaskTable(projectName));
//        String query = "DELETE FROM PROJECTS_LIST WHERE " + projectName;
//        try {
//            Statement state = conn.createStatement();
//            state.execute(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Set the completion status of a project if the project exists. Cannot be called on nonexistent task
     *
     * @param conn already open sqlite database connection
     * @param project project name
     * @param task task name
     * @param status integer status (0 = done, 1 = waiting)
     */
    public static void setStatus(Connection conn, String project, String task, int status) {
        try {
            String query = "UPDATE '" + getTaskTable(project) + "' SET STATUS = " + status + " WHERE NAME = '" + task + "'";
            Statement create = conn.createStatement();
            create.executeUpdate(query);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the owner of a task.
     *
     * @param conn already open connection to an sqlite database
     * @param project already created project name
     * @param task already created task name
     * @param user the owner to be added
     * @return whether adding owner was successful
     */
    public static boolean setUser(Connection conn, String project, String task, String user) {
        try {
            String query = "UPDATE '" + getTaskTable(project) + "' SET OWNER = '" + user + "' WHERE NAME = '" + task + "'";
            Statement create = conn.createStatement();
            create.executeUpdate(query);
            conn.commit();
        } catch (SQLException e) {
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Register a user for a project that already exists
     * @param conn already open connection to the database
     * @param project already created project
     * @param user name of user to register
     * @return whether registration was successful
     */
    public static boolean register(Connection conn, String project, String user) {
        try {
            String query = "INSERT OR REPLACE INTO '" + getEnteredTable(project)
                    + "'(NAME) VALUES('" + user + "')";
            Statement create = conn.createStatement();
            create.execute(query);
            conn.commit();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * Leave a project
     * @param conn already open connection to database
     * @param project already created project
     * @param user user to remove from project
     * @return whether user was successfully removed
     */
    public static boolean leave(Connection conn, String project, String user) {
        try {
            String query = "DELETE FROM '" + getEnteredTable(project) + "' WHERE NAME = '" + user + "'";
            Statement create = conn.createStatement();
            create.executeUpdate(query);
            conn.commit();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
    /**
     * Insert a task into the given project's table of tasks
     *
     * @param conn already open connection to sqlite database
     * @param projectName already created project
     * @param task name of proposed task addition
     * @param start beginning time of task
     * @param end completion time of task
     * @param IP IP task was set from
     * @param port port task was set from
     * @return whether task was added successfully
     */
    public static boolean insertTask(Connection conn, String projectName, String task, String start, String end, String IP, int port) {
        if (!isValidDate(start) || !isValidDate(end)) {
            return false;
        }

        String addTask = "INSERT OR REPLACE INTO '" + getTaskTable(projectName) + "'(NAME, START, END, STATUS, IP, PORT) VALUES('" +
                task + "','" + start + "','" + end + "','" + 0 + "','" + IP + "','" + port + "')";
        try {
            Statement state = conn.createStatement();
            state.executeUpdate(addTask);
            conn.commit();
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Takes an input date and tests it for the specified string pattern. If that pattern is not as expected it returns
     * that the input date is a bad creation of the expected time format.
     * @param date start or end time to be evaluated
     * @return whether string represents a properly formatted date or not
     */
    public static boolean isValidDate(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss-SSS'Z'");
            String update =date.replace('m', '-');
            update = update.replace('s', '-');
            update = update.replace('h', '-');
            Date endTime = dateFormat.parse(update);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Get all of the tasks in a project and return them as a linked list of string arrays
     * @param conn already open sqlite connection
     * @param project project name
     * @return list of strings. List is single string "Failure" if tasks failed to read. Basically escapes throwing
     * exception.
     */
    public static LinkedList<String[]> getTasks(Connection conn, String project) {
        LinkedList<String[]> tasks = new LinkedList<>();
        try {
            Statement create = conn.createStatement();
            ResultSet resultSet = create.executeQuery("SELECT * FROM '" + getTaskTable(project) + "'");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columns = rsmd.getColumnCount();

            while (resultSet.next()) {
                String[] task = new String[columns];
                for (int i = 1; i <= columns; i++) {
                    task[i -1] = resultSet.getString(i);
                }
                tasks.addLast(task);
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * Each project has a different task project list.
     * @param projectName project name
     * @return gets the unique task table name for a project
     */
    private static String getTaskTable(String projectName) {
        //return projectName.replaceAll("[ */&?;]", "__") + "_tasks";
        return projectName + "_tasks";
    }

    /**
     * Each project has a different record of who is entered currently.
     * @param projectName project name
     * @return gets the unique entered table name for a project
     */
    private static String getEnteredTable(String projectName) {
        return projectName + "_entered";
    }
    /**
     * Get the number of tasks associated with a project
     * @param conn already open connection to sqlite database
     * @param project project name
     * @return number of tasks for project
     */
    public static int getNumberTasks(Connection conn, String project) {
        int tasks;
        try {
            String query = "SELECT * FROM " + PROJECTS + " WHERE NAME = '" + project + "'";
            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery(query);
            resultSet.next();
            tasks = Integer.parseInt(resultSet.getString("TASKS"));
            conn.commit();
        } catch (SQLException e) {
            return -1;
        }
        return tasks;
    }

//    public int getNumberProjects(Connection conn) {
//        int projects;
//        try {
//            String query = "SELECT Count(*) FROM " + PROJECTS;
//            Statement state = conn.createStatement();
//            ResultSet resultSet = state.executeQuery(query);
//            conn.commit();
//            resultSet.next();
//            projects = resultSet.getInt(1);
//        } catch (SQLException e) {
//            return -1;
//        }
//        return projects;
//    }

    /**
     * Run on startup to make sure that database is accessible and has a projects table
     * @param dbFile proposed location for database
     * @throws SQLException if the database location is not accessible or writable
     */
    public static void openDatabase(String dbFile) throws SQLException {
        Connection conn;
        String pub = "CREATE TABLE IF NOT EXISTS " + PROJECTS + " (NAME TEXT PRIMARY KEY, TASKS INT NOT NULL)";

        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        Statement state = conn.createStatement();
        state.execute(pub);
        conn.close();
    }

    /**
     * Opens connection to sqlite database with the location given the constructor
     * @return an open sqlite connection to the database
     * @throws SQLException if the database is locked or there are concurrency issues
     */
    public static Connection openConnection(String dbFile) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        c.setAutoCommit(false);
        return c;
    }

    /**
     * Close a connection to the database
     * @param c already open connection to database
     * @throws SQLException if the connection cannot be closed
     */
    public static void closeConnection(Connection c) throws SQLException {
        c.close();
    }











    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Testing functions
/*
    void printAllTables(Connection conn) {
        try {
            Statement create = conn.createStatement();
            ResultSet tables = create.executeQuery("SELECT NAME FROM sqlite_master WHERE type='table'");
            while (tables.next()) {
                String name = tables.getString("NAME");
                System.out.println(name);
//                String query = "SELECT Count(*) FROM " + name;
                //System.out.println(rows);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printAllProjects(Connection conn) {
        try {
            Statement create = conn.createStatement();
            ResultSet resultSet = create.executeQuery("SELECT * FROM " + PROJECTS);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            System.out.println(rsmd.getColumnCount());
            while (resultSet.next()) {
                System.out.println(resultSet.getString("NAME"));
                System.out.println(resultSet.getString("TASKS"));
                //System.out.println(resultSet.getString("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/

    public static void printEntered(Connection conn, String project) {
        printRowsInTable(conn, getEnteredTable(project), true);
    }

    public static void printRowsInTable(Connection conn, String tableName, boolean project_list) {
        try {
            String table = tableName;
            if (!project_list) {
                table = getTaskTable(tableName);
            }

            Statement create = conn.createStatement();
            ResultSet resultSet = create.executeQuery("SELECT * FROM '" + table + "'");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columns = rsmd.getColumnCount();

            for (int i = 1; i <= columns; i++) {
                System.out.println(rsmd.getColumnName(i));
            }

            while (resultSet.next()) {
                StringBuilder flash = new StringBuilder();
                for (int i = 1; i <= columns; i++) {
                    flash.append(resultSet.getString(i) + " ");
                }
                System.out.println(flash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    public void removeTasks(Connection conn, String projectName) {
        try {
            String query = "DROP TABLE IF EXISTS " + getTaskTable(projectName);
            Statement state = conn.createStatement();
            state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    */
    /*
     * Cleaning the database if desired for testing
     *//*

    public void removeTask(Connection conn, String projectName, String task) {
        try {
            String query = "DELETE FROM '" + getTaskTable(projectName) + "' WHERE NAME = '" + task + "'";
            Statement state = conn.createStatement();
            state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getTask(Connection conn, String projectName, String task) {
        String[] retrievedTask = new String[TASKLISTSIZE];
        try {
            Statement create = conn.createStatement();
            ResultSet resultSet = create.executeQuery("SELECT * FROM '" + getTaskTable(projectName)
                    + "' WHERE NAME = '" + task + "'");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columns = rsmd.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                retrievedTask[i-1] = resultSet.getString(i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.flush();
        }

        return retrievedTask;
    }
*/

}
