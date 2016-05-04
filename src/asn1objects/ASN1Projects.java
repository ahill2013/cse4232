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

import datatypes.Projects;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * List of projects in the database by name. Encoded and decoded here.
 */
public class ASN1Projects extends ASNObj {
    public static final int TAGVALUE = 4;

    private Projects projectNames;
    public ASN1Projects() {
        projectNames = new Projects();
    }

    public ASN1Projects(Projects pNames) {
        projectNames = pNames;
    }

    /**
     * Encodes list of projects (strings) as a sequence
     * @return
     */
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();

        for (String projectName : projectNames.getProjectNames()) {
            enc.addToSequence(new Encoder(projectName).setASN1Type(Encoder.TAG_UTF8String));
        }

        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    /**
     * Decodes asnprojects
     * @param dec already created decoder
     * @return list of project names in the database
     * @throws ASN1DecoderFail if a bad byte array/stream is given to the decoder
     */
    @Override
    public Projects decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();

        Projects pNames = new Projects();
        while (!decoder.isEmptyContainer()) {
            pNames.addProjectName(decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String));
        }
        return pNames;
    }
}
