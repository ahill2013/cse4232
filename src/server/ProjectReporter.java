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

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Contains all of the tasks that are being reported to a client for the particular project that
 * the client is listening to
 */
public class ProjectReporter {
    private long latest = 0;
    private Timer scheduled;
    private String projectOwner;

    /**
     * The project is created with already populated lists of tasks (see the UDPEventTracker)
     *
     * @param t list of tasks that the project is going to report
     * @param project name fo the project that you are going to listen to
     */
    ProjectReporter(Timer t, String project) {
        scheduled = t;
        projectOwner = project;
    }

    /**
     * Is this project already scheduled
     * @param project
     * @return wehther it is
     */
    public boolean equals(String project) {
        return projectOwner.equals(project);
    }

    /**
     * Has the last task been reported for the project?
     * @return whether it has
     */
    public boolean active() {
        return latest > new Date().getTime();
    }

    /**
     * If the client does not want to listen to the project kill all of the tasks
     * @return true upon finishing cancelling it
     */
    public boolean  kill() {
        scheduled.cancel();
        return true;
    }

    /**
     * Add a task if it will occur in the next hour to the list of tasks to report
     *
     * @param t the proposed task to be added
     * @return whether the task was added or not
     */
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

    /**
     * Add all of the listed tasks
     * @param tasks
     * @return the number of tasks actually added
     */
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
