import java.io.FileNotFoundException;

/**
 * Created by adam on 2/24/16.
 */



public class TestLogicEngine {

    public static void main(final String[] args) {

        try {
            LogicEngine engine = new LogicEngine("d");

            //engine.parseInput("PROJECT_DEFINITION:Exam;TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2016-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2016-03-15:18h30m00s001Z;2016-03-18:18h30m00s001Z;2016-03-15:20h30m00s001Z");
            //engine.parseInput("TAKE;USER:Johny;PROJECT:Exam;Buy paper");
            //engine.parseInput("GET_PROJECTS");
            //engine.parseInput("GET_PROJECT;Exam");

            engine.closeLogicEngine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}