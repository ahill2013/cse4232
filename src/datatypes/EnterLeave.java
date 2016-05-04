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
