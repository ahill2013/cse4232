package asn1objects;

import datatypes.EnterLeave;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;

/**
 * Created by armin1215 on 4/29/16.
 */
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