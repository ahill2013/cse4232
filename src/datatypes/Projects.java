package datatypes;

import java.util.LinkedList;

/**
 * Created by armin1215 on 3/31/16.
 */
public class Projects {
    private LinkedList<Project> projects;

    public void addProject(Project p) {
        projects.add(p);
    }

    public LinkedList<Project> getProjects() {
        return projects;
    }
}
