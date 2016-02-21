import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by armin1215 on 2/21/16.
 */


public class LogicEngine {
    BackEnd be = new BackEnd();

    public LogicEngine(String dbLocation) throws FileNotFoundException {
        Path p = new Paths.get(dbLocation);
    }
    public String response(String[] in) {
        StringBuilder out = new StringBuilder();

        return out.toString();
    }
}
