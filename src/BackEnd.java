/**
 * Created by armin1215 on 2/21/16.
 */
import java.sql.*;

public class BackEnd {
    private static final String PROJECTS = "PROJECTS_LIST";
    String _dbFile;

    public BackEnd(String dbPath) {
        openDatabase(dbPath);
        _dbFile = dbPath;
    }

    public void createProject(Connection conn, String projectName, int tasks) {
//        String table = "ERROR";
//        String createProject = "CREATE TABLE IF NOT EXISTS " + projectName +
//                "(NAME TEXT NOT NULL, TASKS INT NOT NULL)";


        try {
            String addProjectName = "INSERT OR REPLACE INTO PROJECTS_LIST(NAME, TASKS) VALUES('" + projectName + "','" + tasks + "')";
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


    public String retrieveData() {
        return null;
    }



    private void openDatabase(String dbFile) {
        Connection conn = null;
        String pub = "CREATE TABLE IF NOT EXISTS " + PROJECTS + " (NAME TEXT NOT NULL, TASKS INT NOT NULL)";
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
//                ResultSet rows = create.executeQuery("SELECT Count(*) FROM " + name);
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
