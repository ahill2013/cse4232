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

import datatypes.Take;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Encoding object for a take command
 */
public class ASN1Take extends ASNObj{
    public static final int TAGVALUE = 7;

    private Take _take;

    public ASN1Take() {_take = new Take(" ", " ", " ");}
    public ASN1Take(Take take) {_take = take;}

    /**
     * Encodes take command with project, task, and name of user taking task
     *
     * @return encoded object
     */
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(_take.getPerson()));
        enc.addToSequence(new Encoder(_take.getProject()));
        enc.addToSequence(new Encoder(_take.getTask()));

        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    /**
     * Decoded Take command with specific data
     * @param dec already created decoder
     * @return take object
     * @throws ASN1DecoderFail if byte array or stream is poor
     */
    @Override
    public Take decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        String person = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        String project = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        String task = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        return new Take(person, project, task);
    }
}
