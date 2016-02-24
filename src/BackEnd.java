/**
 * Created by armin1215 on 2/21/16.
 */
import java.sql.*;

public class BackEnd {
    private static final String PROJECTS = "PROJECTS_LIST";
    Connection conn;
    public BackEnd(String dbPath) {
        conn = openConnection(dbPath);
    }

    public void createProject(String projectName, int tasks) {
//        String table = "ERROR";
//        String createProject = "CREATE TABLE IF NOT EXISTS " + projectName +
//                "(NAME TEXT NOT NULL, TASKS INT NOT NULL)";

        String addProjectName = "INSERT INTO PROJECTS_LIST(NAME, TASKS) VALUES('" + projectName + "','" + tasks + "')";
        try {
            Statement create = conn.createStatement();
//            create.execute(createProject);
            create.executeUpdate(addProjectName);

            if (tasks > 0) {
                final String taskTable = getTaskTable(projectName);
                final String dropTable = "DROP TABLE IF EXISTS " + taskTable;
                create.execute(dropTable);


                final String createTable = "CREATE TABLE " + taskTable +
                        "(NAME TEXT NOT NULL, PARENT TEXT NOT NULL, START TEXT NOT NULL, END TEXT NOT NULL)";
                create.execute(createTable);
            }

        } catch (SQLException e) {
            System.err.println("Failed to create project");
            e.printStackTrace();
        }

    }

    public void insertTask(String projectName, String task, String start, String end) {
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



    private Connection openConnection(String dbFile) {
        Connection conn = null;
        String pub = "CREATE TABLE IF NOT EXISTS " + PROJECTS + " (NAME TEXT NOT NULL, TASKS INT NOT NULL)";
        try {
            //TODO check how DriverManager will manage paths to files/folders
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            Statement state = conn.createStatement();
            state.execute(pub);
            ResultSet rs = state.executeQuery("SELECT NAME FROM sqlite_master WHERE type='table'");
            while (rs.next()) {
                System.out.println(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to open database");
            e.printStackTrace();
        }
        return conn;
    }

    void printAllTables() {
        try {
            Statement create = conn.createStatement();
            ResultSet tables = create.executeQuery("SELECT NAME FROM sqlite_master WHERE type='table'");
            while (tables.next()) {
                System.out.println(tables.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printAllProjects() {
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
