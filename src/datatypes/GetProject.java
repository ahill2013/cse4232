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
 * Abstract version of ASN1GetProject
 * Has the name of the requested project and a toString method for the client and server
 * to use as stdout whenever one of these objects is received.
 */
public class GetProject {
    private String _name;

    public GetProject(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("GET_PROJECT;" + _name).toString();
    }

    public String getName() {
        return _name;
    }
}
