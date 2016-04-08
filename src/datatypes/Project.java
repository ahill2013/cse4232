package datatypes;

import java.util.List;

/**
 * Abstract version of project which is the input for an ASN1Project and output of a decoded
 * ASN1 project. The project at a bare minimum needs a name.
 *
 * Project may be printed to the screen and the name and tasks retrieved.
 *
 * Specifically the tasks list is dynamic
 */
public class Project {
    private String name;
    private List<Task> taskList;

    /**
     * Constructor
     * @param name project name
     */
    public Project(String name) {
        this.name = name;
    }

    /**
     * Constructor
     * @param name project name
     * @param tasks list of tasks associated with project
     */
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
    public void addTask(Task t) {
        taskList.add(t);
    }
}
