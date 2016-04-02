package asn1objects;

import asn1.net.ddp2p.ASN1.*;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Created by armin1215 on 3/31/16.
 */
public class ASN1Project extends ASNObj {


    public ASN1Project instance() {
        return new ASN1Project();
    }

    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();

        return enc;
    }

    @Override
    public String[] decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        return null;
    }
}
