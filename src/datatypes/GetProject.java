package datatypes;

/**
 * Created by armin1215 on 4/5/16.
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
