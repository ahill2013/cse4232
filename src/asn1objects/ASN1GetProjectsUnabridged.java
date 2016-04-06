package asn1objects;

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Created by armin1215 on 4/6/16.
 */
public class ASN1GetProjectsUnabridged extends ASNObj {
        public static final int TAGVALUE = 9;

        @Override
        public Encoder getEncoder() {
            Encoder enc = new Encoder();
            return enc.setASN1Type(Encoder.CLASS_PRIVATE, Encoder.PC_PRIMITIVE, (byte) (Encoder.TAG_SEQUENCE + TAGVALUE));
        }

        @Override
        public Integer decode(Decoder dec) throws ASN1DecoderFail {
            return -1;
        }
}