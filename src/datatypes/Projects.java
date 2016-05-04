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
 * List of abbreviated projects returned from the ASN1GetProjects command
 *
 * Empty if unsuccessful retrieval of projects occurs.
 */
public class Projects {
    private List<String> _projectNames;

    /**
     * Constructors with obvious purposes
     */
    public Projects() { _projectNames = new LinkedList<>(); }
    public Projects(List<String> projectNames) {
        _projectNames = projectNames;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Projects:" + _projectNames.size() + ";");

        for (String projectName : _projectNames) {
            sb.append(projectName + ";");
        }

        return sb.toString();
    }

    public void addProjectName(String name) {
        _projectNames.add(name);
    }

    public List<String> getProjectNames() {
        return _projectNames;
    }
}
