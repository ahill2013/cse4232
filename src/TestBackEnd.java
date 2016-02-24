import java.sql.SQLException;

/**
 * Created by armin1215 on 2/23/16.
 */
public class TestBackEnd {

    public static void main(final String[] args) {
        BackEnd be = new BackEnd("/home/armin1215/Networks/test.db");
        int tasks = 2;
        String project = "Testing";
        be.createProject(project, 2);
        String buy = "Buy paper";
        String start_buy = "2016-03-12:18h30m00s001Z";
        String end_buy = "2016-03-15:18h30m00s001Z";

        String write = "Write exam";
        String start_write = "2016-03-15:18h30m00s001z";
        String end_write = "2016-03-15:18h30m00s001z";

        be.insertTask(project, buy, start_buy, end_buy);
        be.insertTask(project, write, start_write, end_write);

        be.printAllProjects();
        be.printAllTables();
        /*try {
            be.conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to close");
        }*/
    }
}
