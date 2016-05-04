package test;

import server.Database.BackEnd;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

public class TestBackEnd {

    public static void main(final String[] args) {

        BackEnd be = null;
        Connection conn = null;
        try {
            BackEnd.openDatabase("/home/armin1215/Classes/Networks/test.db");
            //be = new BackEnd("/home/adam/Documents/NP/Milestone 2/server/cse4232/test.db");
            conn = BackEnd.openConnection("/home/armin1215/Classes/Networks/test.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        int num_tasks = 2;
        String project = "Testing2";
        BackEnd.createProject(conn, project, 2);
        String buy = "Buy paper";
        String start_buy = "2016-03-12:18h30m00s001Z";
        String end_buy = "2016-03-15:18h30m00s001Z";

        String write = "Write exam";
        String start_write = "2016-03-15:18h30m00s001z";
        String end_write = "2016-03-15:18h30m00s001z";

        String IP = "127.0.0.1";
        int port = 2356;

        BackEnd.insertTask(conn, project, buy, start_buy, end_buy, IP, port);
        BackEnd.insertTask(conn, project, write, start_write, end_write, IP, port);

//        be.printAllProjects(conn);
//        be.printAllTables(conn);
//
//        be.printRowsInTable(conn, project, false);

        BackEnd.setUser(conn, project, buy, "Johnny");
        BackEnd.setUser(conn, project, write, "Mary");
        BackEnd.setStatus(conn, project, buy, 1);

        System.out.println(be.getNumberTasks(conn, project));
        System.out.println(be.getNumberTasks(conn, "Exam1"));


//        try {
            LinkedList<String> projects = BackEnd.getAllProjects(conn);
            for (String proj : projects) {
                System.out.println(proj);
            }

            LinkedList<String[]> tasks = BackEnd.getTasks(conn, project);
            for (String[] task : tasks) {
                for (String part : task) {
                    System.out.print(part + " ");
                }
                System.out.println();
            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }

        if (!BackEnd.register(conn, project, "Jim")) {
            System.out.println("Failed to register");
        }

        if (!BackEnd.leave(conn, project, "Jim")) {
            System.out.println("Failed to leave");
        }

        BackEnd.printEntered(conn, project);

        try {
            BackEnd.closeConnection(conn);
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
