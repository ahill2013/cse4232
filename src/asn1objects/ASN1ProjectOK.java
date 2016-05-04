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

import datatypes.ProjectOK;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

public class ASN1ProjectOK extends ASNObj {
    public static final int TAGVALUE = 3;

    ProjectOK projectOK;
    public ASN1ProjectOK() { projectOK = new ProjectOK(); }
    public ASN1ProjectOK(int okays) {
        projectOK = new ProjectOK(okays);
    }

    public ASN1ProjectOK instance() {return new ASN1ProjectOK();}

    /**
     * Encodes list of integers designating status of server queries
     * @return Encoded list of the outcomes received and the success status
     */
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(projectOK.getOkays()).setASN1Type(Encoder.TAG_INTEGER));
        return enc.setASN1Type(Encoder.CLASS_UNIVERSAL,Encoder.PC_PRIMITIVE,(byte) TAGVALUE);
    }

    /**
     * Decodes list and returns as project ok
     * @param dec already created decoder
     * @return list of query outcomes
     * @throws ASN1DecoderFail if a bad byte array/stream is given to decoder
     */
    @Override
    public ProjectOK decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        int ok = decoder.getInteger().intValue();
        return new ProjectOK(ok);
    }
}
