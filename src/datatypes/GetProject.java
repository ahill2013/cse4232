package datatypes;

/**
 * Abstract version of ASN1GetProject
 * Has the name of the requested project and a toString method for the client and server
 * to use as stdout whenever one of these objects is received.
 */
public class GetProject {
    private String _name;

    public GetProject(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("GET_PROJECTS;" + _name).toString();
    }

    public String getName() {
        return _name;
    }
}
