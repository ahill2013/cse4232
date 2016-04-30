package server;

import datatypes.Task;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by armin1215 on 4/29/16.
 */
public class UDPEventTracker {
    private LinkedList<ProjectReporter> scheduled;
    public boolean _running = true;
    private InetAddress _ip;
    private int _port;

    public UDPEventTracker(InetAddress ip, int port) {
        _ip = ip;
        _port = port;
        scheduled = new LinkedList<>();
    }
    public void update() {
        Iterator<ProjectReporter> sch = scheduled.iterator();
        while (sch.hasNext()) {
            ProjectReporter pr = sch.next();
            if (!pr.active()) {
                pr.kill();
                sch.remove();
            }
        }
    }

    public int size() {
        return scheduled.size();
    }
    public ProjectReporter getPR(String project) {
        if (!scheduled.isEmpty()) {
            for (ProjectReporter st : scheduled) {
                if (st.equals(project)) {
                    return st;
                }
            }
        }
        return null;
    }

    public boolean contains(String project) {
        if (!scheduled.isEmpty()) {
            for (ProjectReporter st : scheduled) {
                if (st.equals(project)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int addAllTasks(String project, LinkedList<Task> tasks) {
        ProjectReporter pr = null;
        if (scheduled.contains(project)) {
            pr = getPR(project);
        }
        if (pr == null) {
            pr = new ProjectReporter(new Timer(), project);
        }

        int added = pr.addAllTasks(tasks);
        if (added > 0 ) {
            scheduled.add(pr);
        }
        return added;
    }

    public void remove(LinkedList<String> project) {
        for (String p : project) {
            Iterator<ProjectReporter> i = scheduled.iterator();
            while (i.hasNext()) {
                ProjectReporter pr = i.next();
                if (pr.equals(p)) {
                    pr.kill();
                }
                i.remove();
            }
        }
    }
    public boolean equals(InetAddress ip, int port) {
        return _ip.equals(ip) && _port == port;
    }

//    public void terminate() {
//        for (Timer t : scheduled) {
//            t.cancel();
//        }
//    }

}
