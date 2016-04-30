package datatypes;

import java.util.LinkedList;

/**
 * Created by armin1215 on 4/29/16.
 */
public class EnterLeave {
    private LinkedList<String> projects;
    private boolean enter;

    public EnterLeave(String project, boolean type) {
        projects = new LinkedList<>();
        projects.add(project);
        enter = type;
    }
    public EnterLeave(boolean type) {
        projects = new LinkedList<>();
        enter = type;
    }

    public EnterLeave(LinkedList<String> projects, boolean type) {
        this.projects = projects;
        enter = type;
    }

    public LinkedList<String> getProjects() {
        return projects;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (enter) {
            sb.append("REGISTER;");
        } else {
            sb.append("LEAVE;");
        }
        for (String project : projects) {
            sb.append(project + ";");
        }

        return sb.toString();
    }
}
