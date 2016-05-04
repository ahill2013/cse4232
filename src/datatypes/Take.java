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

/**
 * Command sent from client to set ownership of a task within a project
 *
 * Only encoded by clients and decoded by the server.
 */
public class Take {
    private String _person;
    private String _project;
    private String _task;


    public Take(String person, String project, String task) {
        _person = person;
        _project = project;
        _task = task;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TAKE;USER:" + _person + ";");
        sb.append("PROJECT:" + _project + ";");
        sb.append(_task);
        return sb.toString();
    }

    public String getPerson() { return _person; }
    public String getProject() { return _project; }
    public String getTask() { return _task; }

}
