/**
 * Created by armin1215 on 2/21/16.
 */
import java.io.File;
import java.sql.*;
import org.sqlite.JDBC;

public class BackEnd {
    Connection db;
    public BackEnd(File dbFile) {
        db = openConnection();
    }

    public String createProject() {
        return null;
    }

    public String retrieveData() {
        return null;
    }
    private Connection openConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:projects.db");

        } catch (SQLException e) {
            System.err.println("Failed to open database");
            e.printStackTrace();
        }
        return conn;
    }

}
