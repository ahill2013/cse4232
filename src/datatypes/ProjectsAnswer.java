package datatypes;


import java.util.LinkedList;
import java.util.List;

/**
 * Response to unabridged getprojects request. Effectively returns
 * everything in the database.
 *
 * Only returned to the client after and ASN1GetProjectsUnabridged is
 * received by the server
 */
public class ProjectsAnswer {
    private List<Project> _projects;


    public ProjectsAnswer() {
        _projects = new LinkedList<>();
    }
    public ProjectsAnswer(List<Project> projects) {
        _projects = projects;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OKProjects:" + _projects.size() + ";");

        for (Project p : _projects) {
            sb.append(p.toString());
        }
        return sb.toString();
    }
    public void addProject(Project p) {
        _projects.add(p);
    }

    public List<Project> getProjects() {
        return _projects;
    }
}
