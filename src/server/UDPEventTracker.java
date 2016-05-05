/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2016
                Author:  wnyffenegger2013@my.fit.edu
                Author:  ahill2013@my.fit.edu
                Florida Tech, Computer Science

       This program is free software; you can redistribute it and/or modify
       it under the terms of the GNU Affero General Public License as published by
       the Free Software Foundation; either the current version of the License, or
       (at your option) any later version.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.

      You should have received a copy of the GNU Affero General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */
/* ------------------------------------------------------------------------- */

package server;

import datatypes.Task;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Instance exists for each client and contains all of the projects and tasks that
 * the client is currently listening to.
 */
public class UDPEventTracker {
    /**
     * List of projects that the client is listening to
     */
    private LinkedList<ProjectReporter> scheduled;
    public boolean _running = true;

    /**
     * IP and port of the client
     */
    private InetAddress _ip;
    private int _port;

    /**
     * Set all of the fields
     * @param ip ip of the client
     * @param port port of the client
     */
    public UDPEventTracker(InetAddress ip, int port) {
        _ip = ip;
        _port = port;
        scheduled = new LinkedList<>();
    }

    /**
     * Check to see if all of the tasks in a project have begun. If so then remove the project from the event tracker.
     */
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

    /**
     * Get the number of projects that are being listened to
     * @return list number of projects
     */
    public int size() {
        return scheduled.size();
    }

    /**
     *
     * @param project
     * @return
     */
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

    /**
     * See if the event tracker already contains an object with tasks for a project
     * @param project
     * @return whether the project is already scheduled
     */
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

    /**
     * Add all of the tasks associated with a project. If a ProjectReporter already exists then add the tasks
     * to the project reporter. Otherwise create a new project reporter.
     * @param project project name which is unique in database
     * @param tasks list of tasks to be added
     * @return  number of tasks added to the event tracker
     */
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

    /**
     * Remove all projects input from the event tracker and cancel reporting all of their tasks to the client
     * @param project
     */
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

    /**
     * Check if the client has already asked to listen to a project
     * @param ip the remote host of the client
     * @param port the remote port of the client
     * @return whether the ip and port matches the event trackers ip and port
     */
    public boolean equals(InetAddress ip, int port) {
        return _ip.equals(ip) && _port == port;
    }

}
