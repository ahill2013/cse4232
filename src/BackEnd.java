/**
 * Created by armin1215 on 2/21/16.
 */

import org.tmatesoft.sqljet.core.*;
import org.tmatesoft.sqljet.core.table.*;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;

public class BackEnd {
    SqlJetDb db;
    public BackEnd(File dbFile) {
        try {
            db = SqlJetDb.open(dbFile, true);
        } catch (SqlJetException e) {
            System.err.println("Failed to open DB");
            e.printStackTrace();
        }
    }

    public boolean createProject() {

    }




}
