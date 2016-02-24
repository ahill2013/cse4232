import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by armin1215 on 2/21/16.
 */


public class LogicEngine {
    private BackEnd be;
    private File dbFile;

    public LogicEngine(String dbLocation) throws FileNotFoundException {
        dbFile = new File(dbLocation);
        //be = new BackEnd(dbFile);
    }

    public String response(String[] in) {
        StringBuilder out = new StringBuilder();

        return out.toString();
    }

    private String parseInput(String[] in) {
        return null;
    }
}
