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
