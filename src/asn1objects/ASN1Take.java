package asn1objects;

import datatypes.Take;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Created by armin1215 on 4/5/16.
 */
public class ASN1Take extends ASNObj{
    public static final int TAGVALUE = Encoder.TAG_SEQUENCE + 7;

    private Take _take;

    public ASN1Take() {_take = new Take(" ", " ", " ");}
    public ASN1Take(Take take) {_take = take;}

    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(_take.getPerson()));
        enc.addToSequence(new Encoder(_take.getProject()));
        enc.addToSequence(new Encoder(_take.getTask()));

        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    @Override
    public Take decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        String person = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        String project = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        String task = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        return new Take(person, project, task);
    }
}
