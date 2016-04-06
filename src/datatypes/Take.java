package datatypes;

/**
 * Created by armin1215 on 4/5/16.
 */
public class Take {
    private String _person;
    private String _project;
    private String _task;


    public Take(String person, String project, String task) {
        _person = person;
        _project = project;
        _task = task;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TAKE;USER:" + _person + ";");
        sb.append("PROJECT:" + _project + ";");
        sb.append(_task);
        return sb.toString();
    }

    public String getPerson() { return _person; }
    public String getProject() { return _project; }
    public String getTask() { return _task; }

}
