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

import java.sql.*;

public class BackEnd {
    private static final String PROJECTS = "PROJECTS_LIST";
    String _dbFile;

    public BackEnd(String dbPath) {
        openDatabase(dbPath);
        _dbFile = dbPath;
    }

    public void createProject(Connection conn, int ID, String projectName, int tasks) {
//        String table = "ERROR";
//        String createProject = "CREATE TABLE IF NOT EXISTS " + projectName +
//                "(NAME TEXT NOT NULL, TASKS INT NOT NULL)";


        try {
            String addProjectName = "INSERT OR REPLACE INTO PROJECTS_LIST(ID, NAME, TASKS) VALUES('" + ID + "','" + projectName + "','" + tasks + "')";
            Statement create = conn.createStatement();
//            create.execute(createProject);
            create.executeUpdate(addProjectName);
            conn.commit();
            if (tasks > 0) {
                final String taskTable = getTaskTable(projectName);
                final String dropTable = "DROP TABLE IF EXISTS " + taskTable;
                create.execute(dropTable);
                conn.commit();

                final String createTable = "CREATE TABLE " + taskTable +
                        "(NAME TEXT NOT NULL, PARENT TEXT NOT NULL, START TEXT NOT NULL, END TEXT NOT NULL)";
                create.execute(createTable);
                conn.commit();
            }
        } catch (SQLException e) {
            System.err.println("Failed to create project");
            e.printStackTrace();
        }

    }

    public void removeProject(Connection conn, String projectName) {
        removeTask(conn, getTaskTable(projectName));
        String query = "DELETE FROM PROJECTS_LIST WHERE " + projectName;
        try {
            Statement state = conn.createStatement();
            state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeTask(Connection conn, String taskName) {
        try {
            String query = "DROP TABLE IF EXISTS " + getTaskTable(taskName);
            Statement state = conn.createStatement();
            state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertTask(Connection conn, String projectName, String task, String start, String end) {
        String addTask = "INSERT INTO " + getTaskTable(projectName) + "(NAME, PARENT, START, END) VALUES('" +
                task + "','" + projectName + "','" + start + "','" + end + "')";
        try {
            Statement state = conn.createStatement();
            state.executeUpdate(addTask);
        } catch (SQLException e) {
            System.err.println("Failed to insert task" + projectName + " " + " " + task + " " + start + " " + end);
            e.printStackTrace();
        }
    }

    public String[][] getTasks(String projectName) {
        return null;
    }

    private String getTaskTable(String projectName) {
        return projectName + "_tasks";
    }


    public String retrieveTasks() {
        return null;
    }



    private void openDatabase(String dbFile) {
        Connection conn = null;
        String pub = "CREATE TABLE IF NOT EXISTS " + PROJECTS + " (ID INT NOT NULL, NAME TEXT NOT NULL, TASKS INT NOT NULL)";
        try {
            //TODO check how DriverManager will manage paths to files/folders
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            conn.setAutoCommit(false);
            Statement state = conn.createStatement();
            state.execute(pub);
            conn.commit();
            // ResultSet rs = state.executeQuery("SELECT NAME FROM sqlite_master WHERE type='table'");
            /*while (rs.next()) {
                System.out.println(rs.getString("NAME"));
            }*/
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to open database");
            e.printStackTrace();
        }
    }

    public Connection openConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + _dbFile);
        c.setAutoCommit(false);
        return c;
    }

    public void closeConnection(Connection c) throws SQLException {
        c.close();
    }

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
            while (resultSet.next()) {
                System.out.println(resultSet.getString("NAME"));
                System.out.println(resultSet.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getI(Connection conn) throws SQLException {
        Statement state = conn.createStatement();
        ResultSet rows = state.executeQuery("SELECT Count(*) FROM PROJECTS_LIST");
        rows.next();
        return Integer.parseInt(rows.getString(1));
    }


}
