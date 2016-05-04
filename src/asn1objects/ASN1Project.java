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
import datatypes.Task;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;
import java.util.List;

/**
 * Produces encoders for projects and decodes ASN1Projects.
 *
 * Contains a Project object with all tasks
 */
public class ASN1Project extends ASNObj {

    //Unique tag value for object (for project's scope)
    public static final int TAGVALUE = 2;
    private Project _project;

    @Override
    public ASN1Project instance() {
        return new ASN1Project(" ", null);
    }

    public ASN1Project() {
        this("Empty", null);
    }
    public ASN1Project(Project p) {
        _project = p;
    }
    public ASN1Project(String name, LinkedList<Task> tasks) {
        _project = new Project(name, tasks);
    }

    /**
     * Encodes project and tasks iteratively (entered in by constructor)
     * @return encoded objects
     */
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(_project.getName()).setASN1Type(Encoder.TAG_UTF8String));

        // Iteratively encodes all Tasks into sequence
        for (Task t : _project.getTasks()) {
            enc.addToSequence(new ASN1Task(t).getEncoder().setASN1Type((byte) ASN1Task.TAGVALUE));
        }
        return enc.setASN1Type(Encoder.CLASS_APPLICATION, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    /**
     * Decodes a project
     * @param dec already created decoder
     * @return Project with all of its tasks
     * @throws ASN1DecoderFail if a poor byte stream was given to the decoder
     */
    @Override
    public Project decode(Decoder dec) throws ASN1DecoderFail {

        String name;
        LinkedList<Task> tasks = new LinkedList<Task>();
        Decoder decoder = dec.getContent();
        name = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        while (!decoder.isEmptyContainer()) {
            tasks.add(new ASN1Task().decode(decoder.getFirstObject(true)));
        }
        return new Project(name, tasks);
    }
}
