package server;

import datatypes.Task;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by armin1215 on 4/29/16.
 */
public class ProjectReporter {
    private Timer scheduled;
    private String projectOwner;

    ProjectReporter(Timer t, String project) {
        scheduled = t;
        projectOwner = project;
    }

    public boolean equals(String project) {
        return projectOwner.equals(project);
    }

    public boolean  kill() {
        scheduled.cancel();
        return true;
    }

    public void addTask(Task t) {

        Date d = new Date();
        long time = t.getEndTime().getTime() - d.getTime();
        if (time > 0 && time < 60000) {
            scheduled.schedule(new SendTracked(t), time);
        }
    }
    public boolean addAllTasks(LinkedList<Task> tasks) {
        for (Task t : tasks) {
            addTask(t);
        }
        return true;
    }
}
