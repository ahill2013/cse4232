package test;

import server.BackEnd;
import server.LogicEngine;

import java.sql.SQLException;

public class TestLogicEngine {

    public static void main(final String[] args) {

        try {
            BackEnd.openDatabase("/home/armin1215/Networks/test.db");
            LogicEngine engine = new LogicEngine("/home/armin1215/Networks/test.db");
            //LogicEngine engine = new LogicEngine("/home/adam/Documents/NP/Milestone 2/server/cse4232/test.db");

            String projectCreation = engine.parseInput("PROJECT_DEFINITION:Exam;TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2016-03-15:18h30m00s001Z;" +
                    "Write exam;2016-03-15:18h30m00s001Z;2016-03-15:18h30m00s001Z;TAKE;USER:Johny;PROJECT:Exam;Buy paper" +
                    ";GET_PROJECTS;GET_PROJECT;Exam",
                    "127.0.0.1", 2356);
            System.out.print(projectCreation);
            String ownerCreation = engine.parseInput("TAKE;USER:Johny;PROJECT:Exam;Buy paper", "127.0.0.1", 2356);
            System.out.print(ownerCreation);

            String getProjects = engine.parseInput("GET_PROJECTS", "127.0.0.1", 2356);
            System.out.print(getProjects);

            String listTasks = engine.parseInput("GET_PROJECT;Exam", "127.0.0.1", 2356);
            System.out.print(listTasks);

 //           engine.printDatabase();
            engine.closeLogicEngine();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}