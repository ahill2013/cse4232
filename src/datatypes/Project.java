package datatypes;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by armin1215 on 3/31/16.
 */
public class Project {
    private String name;
    private List<Task> taskList;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.taskList = tasks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("PROJECT_DEFINITION:");
        sb.append(name);
        sb.append("TASKS:" + taskList.size() + ";");
        for (Task t : taskList) {
            sb.append(t.toString());
        }

        return sb.toString();
    }
    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return taskList;
    }
}
