package datatypes;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by armin1215 on 4/5/16.
 */
public class ProjectOK {
    private List<Integer> _okays;

    public ProjectOK() { _okays = new LinkedList<>(); }
    public ProjectOK(List<Integer> okays) {
        _okays = okays;
    }

    public void addOkay(int okay) {
        _okays.add(okay);
    }

    public List<Integer> getOkays() {
        return _okays;
    }

}
