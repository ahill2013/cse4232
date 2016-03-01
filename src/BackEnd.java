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
import java.util.LinkedList;

public class BackEnd {

    /**
     * Hard coded PROJECTS_LIST which lists the project names and the number of tasks associated with them.
     * With a hardcoded task list size used for counting columns for output
     * The String file is in case the connection is closed and someone wishes to reopen the connection.
     */
    private static final String PROJECTS = "PROJECTS_LIST";
    private static final int TASKLISTSIZE = 7;
    public String _dbFile;

    /**
     * Creates database if not already created with projects_list
     *
     * @param dbPath the relative path to the file. May error if program does not have write permissions to location.
     * @throws SQLException error caused when database is locked, does not exist, or directory does not exist
     * or permissions are not given
     */
    public BackEnd(String dbPath) throws SQLException {
        openDatabase(dbPath);
        _dbFile = dbPath;
    }

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
    public boolean createProject(Connection conn, String projectName, int tasks) {
        try {
            String addProjectName = "REPLACE INTO PROJECTS_LIST(NAME, TASKS) VALUES('" + projectName + "','" + tasks + "')";
            Statement create = conn.createStatement();
            create.executeUpdate(addProjectName);
            if (tasks > 0) {
                String taskTable = getTaskTable(projectName);
                String dropTable = "DROP TABLE IF EXISTS '" + taskTable + "'";
                create.execute(dropTable);
                conn.commit();
                printAllTables(conn);
                System.out.println(taskTable);
                final String createTable = "CREATE TABLE '" + taskTable +
                        "'(NAME TEXT NOT NULL, START TEXT NOT NULL, END TEXT NOT NULL, OWNER TEXT," +
                        " STATUS INT NOT NULL, IP TEXT NOT NULL, PORT INT NOT NULL)";
                create.execute(createTable);
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Go to the PROJECTS_LIST table and return all of the projects as a list of strings (names)
     * @param conn already opened connection to an sqlite database
     * @return string of names. Returns "Failure" as first and only string in list if it fails to read from the database
     * This is weird but also suprisingly useful.
     */
    LinkedList<String> getAllProjects(Connection conn) {
        LinkedList<String> projects = new LinkedList<>();
        try {
            Statement create = conn.createStatement();
            ResultSet resultSet = create.executeQuery("SELECT * FROM " + PROJECTS);
            conn.commit();
            while (resultSet.next()) {
                projects.addLast(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            projects = new LinkedList<>();
            projects.add("Failure");
        }

        return projects;

    }


    /**
     * For use later
     * @param conn
     * @param projectName
     */
    public void removeProject(Connection conn, String projectName) {
        removeTasks(conn, getTaskTable(projectName));
        String query = "DELETE FROM PROJECTS_LIST WHERE " + projectName;
        try {
            Statement state = conn.createStatement();
            state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the completion status of a project if the project exists. Cannot be called on nonexistent task
     *
     * @param conn already open sqlite database connection
     * @param project project name
     * @param task task name
     * @param status integer status (0 = done, 1 = waiting)
     */
    public void setStatus(Connection conn, String project, String task, int status) {
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
    public boolean setUser(Connection conn, String project, String task, String user) {
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
    public boolean insertTask(Connection conn, String projectName, String task, String start, String end, String IP, int port) {
        String addTask = "INSERT OR REPLACE INTO '" + getTaskTable(projectName) + "'(NAME, START, END, STATUS, IP, PORT) VALUES('" +
                task + "','" + start + "','" + end + "','" + 0 + "','" + IP + "','" + port + "')";
        try {
            Statement state = conn.createStatement();
            state.executeUpdate(addTask);
            conn.commit();
        } catch (SQLException e) {
//            e.printStackTrace();
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
    public LinkedList<String[]> getTasks(Connection conn, String project) {
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
//            e.printStackTrace();
            tasks = new LinkedList<>();
            String[] f = new String[1];
            f[0] = "Failure";
            tasks.addFirst(f);
        }
        return tasks;
    }

    /**
     * Each project has a different task project list. Problem queries have their issue characters replaced
     * @param projectName
     * @return
     */
    private String getTaskTable(String projectName) {
        //return projectName.replaceAll("[ */&?;]", "__") + "_tasks";
        return projectName + "_tasks";
    }

    public int getNumberTasks(Connection conn, String project) {
        try {
            String query = "SELECT * FROM " + PROJECTS + " WHERE NAME = '" + project + "'";
            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery(query);
            conn.commit();
            resultSet.next();
            return resultSet.getInt("TASKS");
        } catch (SQLException e) {
            return -1;
        }
    }

    private void openDatabase(String dbFile) throws SQLException {
        Connection conn = null;
        String pub = "CREATE TABLE IF NOT EXISTS " + PROJECTS + " (NAME TEXT PRIMARY KEY, TASKS INT NOT NULL)";

        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        Statement state = conn.createStatement();
        state.execute(pub);
        conn.close();
    }

    public Connection openConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + _dbFile);
        c.setAutoCommit(false);
        return c;
    }

    public void closeConnection(Connection c) throws SQLException {
        c.close();
    }











    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Testing functions
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
                //System.out.println(resultSet.getString("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printRowsInTable(Connection conn, String tableName, boolean project_list) {
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

    public void removeTasks(Connection conn, String projectName) {
        try {
            String query = "DROP TABLE IF EXISTS " + getTaskTable(projectName);
            Statement state = conn.createStatement();
            state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     * Cleaning the database if desired for testing
     */
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

}
