import java.io.FileNotFoundException;

/**
 * Created by adam on 2/24/16.
 */



public class TestLogicEngine {

    public static void main(final String[] args) {

        try {
            LogicEngine engine = new LogicEngine("/home/armin1215/Networks/test.db");

            String projectCreation = engine.parseInput("PROJECT_DEFINITION:Exam;TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2016-03-15:18h30m00s001Z;" +
                    "Write exam;2016-03-15:18h30m00s001Z;2016-03-15:18h30m00s001Z;",
                    "127.0.0.1", 2356);
            System.out.print(projectCreation);
            String ownerCreation = engine.parseInput("TAKE;USER:Johny;PROJECT:Exam;Buy paper", "127.0.0.1", 2356);
            System.out.print(ownerCreation);

            String getProjects = engine.parseInput("GET_PROJECTS", "127.0.0.1", 2356);
            System.out.print(getProjects);

            String listTasks = engine.parseInput("GET_PROJECT;Exam", "127.0.0.1", 2356);
            System.out.print(listTasks);

            engine.printDatabase();
            engine.closeLogicEngine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}