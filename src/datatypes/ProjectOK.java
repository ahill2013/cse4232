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
 * Abstract version of ASN1ProjectOK. Contains list of the outcomes of the server's attempts
 * to decode and interact with database for successfully decoded tasks.
 *
 * 0 is a successful reception. Anything else is a failure. Most failures are logic errors because
 * the client's handle encoding messages.
 */
public class ProjectOK {
    private int _okays;

    public ProjectOK() { _okays = -1; }
    public ProjectOK(int okays) {
        _okays = okays;
    }

    public void addOkay(int okay) {
        _okays = okay;
    }

    public int getOkays() {
        return _okays;
    }

}
