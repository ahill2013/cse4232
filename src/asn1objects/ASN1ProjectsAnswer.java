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

package asn1objects;

import datatypes.Project;
import datatypes.ProjectsAnswer;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;
import java.util.List;

/**
 * List all of the projects and their respective tasks in the database.
 *
 * Basically a database dump.
 */
public class ASN1ProjectsAnswer {
    public static final int TAGVALUE = 5;

    private ProjectsAnswer _projectAns;

    public ASN1ProjectsAnswer(ProjectsAnswer p) {
        _projectAns = p;
    }

    public ASN1ProjectsAnswer() {
        _projectAns = new ProjectsAnswer(null);
    }

    public ASN1ProjectsAnswer instance() {
        return new ASN1ProjectsAnswer();
    }


    /**
     * Encode all of the projects as objects and send them
     * @return encoded objects
     */
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();

        for (Project p : _projectAns.getProjects()) {
            enc.addToSequence(new ASN1Project(p).getEncoder().setASN1Type(Encoder.TAG_SEQUENCE));
        }
        return enc.setASN1Type(Encoder.CLASS_PRIVATE, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    /**
     * Decode all of the projects and tasks in the database
     *
     * @param dec already created decoder with projectsanswer
     * @return list of all projects and tasks in database
     * @throws ASN1DecoderFail if byte array/stream entered is poor
     */
    public ProjectsAnswer decode(Decoder dec) throws ASN1DecoderFail {
        List<Project> projects = new LinkedList<>();
        Decoder decoder = dec.getContent();

        while(!decoder.isEmptyContainer()) {
            projects.add(new ASN1Project().decode(decoder.getFirstObject(true)));
        }

        return new ProjectsAnswer(projects);
    }
}
