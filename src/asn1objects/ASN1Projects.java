package asn1objects;

import datatypes.Projects;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;
import java.util.List;

/**
 * Created by armin1215 on 3/31/16.
 */
public class ASN1Projects extends ASNObj {

    public ASN1Projects() {}

    @Override
    public ASN1Projects instance() {
        return new ASN1Projects();
    }

    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, Encoder.TAG_SEQUENCE);
    }

    @Override
    public Projects decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        return new Projects();
    }
}
