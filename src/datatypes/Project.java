package datatypes;

import java.util.LinkedList;

/**
 * Created by armin1215 on 3/31/16.
 */
public class Project {
    private String name;
    private LinkedList<Task> taskList;

    public Project(String name, LinkedList<Task> tasks) {
        this.name = name;
        this.taskList = tasks;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Task> getTasks() {
        return taskList;
    }
}
