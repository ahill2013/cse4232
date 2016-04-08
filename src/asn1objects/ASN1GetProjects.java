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
