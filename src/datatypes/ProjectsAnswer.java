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
 * Response to unabridged getprojects request. Effectively returns
 * everything in the database.
 *
 * Only returned to the client after and ASN1GetProjectsUnabridged is
 * received by the server
 */
public class ProjectsAnswer {
    private List<Project> _projects;


    public ProjectsAnswer() {
        _projects = new LinkedList<>();
    }
    public ProjectsAnswer(List<Project> projects) {
        _projects = projects;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OKProjects:" + _projects.size() + ";");

        for (Project p : _projects) {
            sb.append(p.toString());
        }
        return sb.toString();
    }
    public void addProject(Project p) {
        _projects.add(p);
    }

    public List<Project> getProjects() {
        return _projects;
    }
}
