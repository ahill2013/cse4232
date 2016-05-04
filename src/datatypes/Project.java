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

package datatypes;

import java.util.LinkedList;
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
    private LinkedList<Task> taskList;

    /**
     * Constructor
     * @param name project name
     */
    public Project(String name) {
        this.name = name;
        this.taskList = new LinkedList<>();
    }

    /**
     * Constructor
     * @param name project name
     * @param tasks list of tasks associated with project
     */
    public Project(String name, LinkedList<Task> tasks) {
        this.name = name;
        this.taskList = tasks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("PROJECT_DEFINITION:");
        sb.append(name);
        sb.append(";TASKS:" + taskList.size() + ";");
        for (Task t : taskList) {
            sb.append(t.toString());
        }

        return sb.toString();
    }
    public String getName() {
        return name;
    }

    public LinkedList<Task> getTasks() {
        return taskList;
    }
    public void addTask(Task t) {
        taskList.add(t);
    }

    public void addPort(int port) {
        for (Task task : taskList) {
            task.setPort(port);
        }
    }

    public void addIP(String IP) {
        for (Task task : taskList) {
            task.setIP(IP);
        }
    }
}
