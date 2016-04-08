package datatypes;

import java.util.LinkedList;
import java.util.List;

/**
 * List of abbreviated projects returned from the ASN1GetProjects command
 *
 * Empty if unsuccessful retrieval of projects occurs.
 */
public class Projects {
    private List<String> _projectNames;

    /**
     * Constructors with obvious purposes
     */
    public Projects() { _projectNames = new LinkedList<>(); }
    public Projects(List<String> projectNames) {
        _projectNames = projectNames;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Projects:" + _projectNames.size() + ";");

        for (String projectName : _projectNames) {
            sb.append(projectName + ";");
        }

        return sb.toString();
    }

    public void addProjectName(String name) {
        _projectNames.add(name);
    }

    public List<String> getProjectNames() {
        return _projectNames;
    }
}
