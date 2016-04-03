package asn1objects;

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by armin1215 on 3/31/16.
 */
public class ASN1ProjectOK extends ASNObj {

    List<Integer> _okays = new LinkedList<>();
    public ASN1ProjectOK() {}
    public ASN1ProjectOK(List<Integer> okays) {_okays.addAll(okays);}

    public ASN1ProjectOK instance() {return new ASN1ProjectOK();}

    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        for (Integer okay : _okays) {
            enc.addToSequence(new Encoder(okay).setASN1Type(Encoder.TAG_INTEGER));
        }
        return enc.setASN1Type(Encoder.CLASS_UNIVERSAL,Encoder.PC_PRIMITIVE,Encoder.TAG_INTEGER);
    }

    @Override
    public List<Integer> decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        List<Integer> okays = new LinkedList<>();
        while(!decoder.isEmptyContainer()) {
            okays.add(decoder.getFirstObject(true).getInteger().intValue());
        }
        return okays;
    }
}
