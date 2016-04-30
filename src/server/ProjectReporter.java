package server;

import datatypes.Task;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by armin1215 on 4/29/16.
 */
public class ProjectReporter {
    private long latest = 0;
    private Timer scheduled;
    private String projectOwner;

    ProjectReporter(Timer t, String project) {
        scheduled = t;
        projectOwner = project;
    }

    public boolean equals(String project) {
        return projectOwner.equals(project);
    }

    public boolean active() {
        return latest > new Date().getTime();
    }
    public boolean  kill() {
        scheduled.cancel();
        return true;
    }

    public boolean addTask(Task t) {

        Date d = new Date();
        long time = t.getStartTime().getTime() - d.getTime();

        if (time > 0 && time < 3600000) {
            if (t.getStartTime().getTime() > latest) {
                latest = t.getStartTime().getTime();
            }
            scheduled.schedule(new SendTracked(projectOwner, t), time);
            return true;
        }
        return false;
    }
    public int addAllTasks(LinkedList<Task> tasks) {
        int count = 0;
        for (Task t : tasks) {
            if(addTask(t)) {
                count++;
            }
        }
        return count;
    }
}
