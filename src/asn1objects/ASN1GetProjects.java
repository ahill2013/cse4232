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

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Empty asn1 packet sent as request for list of projects
 *
 * Tag value is unique in project.
 */
public class ASN1GetProjects extends ASNObj {
    //Unique tag value for object (for project's scope)
    public static final int TAGVALUE = 8;
    private static final String DEFAULT = "DEFAULT";

    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(DEFAULT).setASN1Type(Encoder.TAG_UTF8String));
        return enc.setASN1Type(Encoder.CLASS_PRIVATE, Encoder.PC_PRIMITIVE, (byte) TAGVALUE);
    }

    @Override
    public String decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        String check = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        return check;
    }
}
