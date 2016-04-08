package datatypes;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract version of ASN1ProjectOK. Contains list of the outcomes of the server's attempts
 * to decode and interact with database for successfully decoded tasks.
 *
 * 0 is a successful reception. Anything else is a failure. Most failures are logic errors because
 * the client's handle encoding messages.
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
