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

import datatypes.EnterLeave;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;

public class ASN1Leave extends ASNObj {
    public static final int TAGVALUE = 11;
    private EnterLeave _el;

    public ASN1Leave() {}
    public ASN1Leave(EnterLeave el) {
        _el = el;
    }
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();

        for (String project : _el.getProjects()) {
            enc.addToSequence(new Encoder(project).setASN1Type(Encoder.TAG_UTF8String));
        }
        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    @Override
    public EnterLeave decode(Decoder dec) throws ASN1DecoderFail {
        LinkedList<String> el = new LinkedList<>();

        Decoder decoder = dec.getContent();

        while (!decoder.isEmptyContainer()) {
            el.add(decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String));
        }

        return new EnterLeave(el, false);
    }
}