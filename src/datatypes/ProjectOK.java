package datatypes;

/**
 * Abstract version of ASN1ProjectOK. Contains list of the outcomes of the server's attempts
 * to decode and interact with database for successfully decoded tasks.
 *
 * 0 is a successful reception. Anything else is a failure. Most failures are logic errors because
 * the client's handle encoding messages.
 */
public class ProjectOK {
    private int _okays;

    public ProjectOK() { _okays = -1; }
    public ProjectOK(int okays) {
        _okays = okays;
    }

    public void addOkay(int okay) {
        _okays = okay;
    }

    public int getOkays() {
        return _okays;
    }

}
