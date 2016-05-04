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

import datatypes.GetProject;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Request sent for a particular project. If project doesn't exist a ProjectOK
 * failure will be registered.
 *
 * getEncoder and decode are straightforward
 */
public class ASN1GetProject extends ASNObj {
    //Unique tag value for object (for project's scope)
    public static final int TAGVALUE = 6;
    private GetProject _getProject;

    public ASN1GetProject() {}
    public ASN1GetProject(GetProject gp) {
        _getProject = gp;
    }


    /**
     * Encodes getProject command
     * @return
     */
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(_getProject.getName()).setASN1Type(Encoder.TAG_UTF8String));
        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    /**
     * Decodes GetProject command
     *
     * @param dec already created decoder
     * @return GetProject request with project name
     * @throws ASN1DecoderFail if a poor byte array is given
     */
    @Override
    public GetProject decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        String projectName = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        return new GetProject(projectName);
    }
}
