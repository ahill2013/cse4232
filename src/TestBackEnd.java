import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by armin1215 on 2/23/16.
 */
public class TestBackEnd {

    public static void main(final String[] args) {

        BackEnd be = null;
        Connection conn = null;
        try {
            be = new BackEnd("/home/armin1215/Networks/test.db");
            //be = new BackEnd("/home/adam/Documents/NP/Milestone 2/server/cse4232/test.db");
            conn = be.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        int num_tasks = 2;
        String project = "Testing2";
        be.createProject(conn, project, 2);
        String buy = "Buy paper";
        String start_buy = "2016-03-12:18h30m00s001Z";
        String end_buy = "2016-03-15:18h30m00s001Z";

        String write = "Write exam";
        String start_write = "2016-03-15:18h30m00s001z";
        String end_write = "2016-03-15:18h30m00s001z";

        String IP = "127.0.0.1";
        int port = 2356;

        be.insertTask(conn, project, buy, start_buy, end_buy, IP, port);
        be.insertTask(conn, project, write, start_write, end_write, IP, port);

        be.printAllProjects(conn);
        be.printAllTables(conn);

        be.printRowsInTable(conn, project, false);

        be.setUser(conn, project, buy, "Johnny");
        be.setUser(conn, project, write, "Mary");
        be.setStatus(conn, project, buy, 1);

        System.out.println(be.getNumberTasks(conn, project));
        System.out.println(be.getNumberTasks(conn, "Exam1"));


        LinkedList<String> projects = be.getAllProjects(conn);
        for (String proj : projects) {
            System.out.println(proj);
        }

        LinkedList<String[]> tasks = be.getTasks(conn, project);
        for (String[] task : tasks) {
            for (String part : task) {
                System.out.print(part + " ");
            }
            System.out.println();
        }

        try {
            be.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*try {
            be.conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to close");
        }*/
    }
}
