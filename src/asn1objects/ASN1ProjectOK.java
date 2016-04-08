/**
 * Created by armin1215 on 3/31/16.
 */

package asn1objects;

import datatypes.ProjectOK;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;
import java.util.List;

public class ASN1ProjectOK extends ASNObj {
    public static final int TAGVALUE = Encoder.TAG_SEQUENCE + 3;

    ProjectOK projectOK;
    public ASN1ProjectOK() { projectOK = new ProjectOK(); }
    public ASN1ProjectOK(List<Integer> okays) {
        projectOK = new ProjectOK(okays);
    }

    public ASN1ProjectOK instance() {return new ASN1ProjectOK();}

    /**
     * Encodes list of integers designating status of server queries
     * @return Encoded list of the outcomes received and the success status
     */
    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        for (Integer okay : projectOK.getOkays()) {
            enc.addToSequence(new Encoder(okay).setASN1Type(Encoder.TAG_INTEGER));
        }
        return enc.setASN1Type(Encoder.CLASS_UNIVERSAL,Encoder.PC_PRIMITIVE,(byte) TAGVALUE);
    }

    /**
     * Decodes list and returns as project ok
     * @param dec already created decoder
     * @return list of query outcomes
     * @throws ASN1DecoderFail if a bad byte array/stream is given to decoder
     */
    @Override
    public ProjectOK decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();
        List<Integer> okays = new LinkedList<>();
        while(!decoder.isEmptyContainer()) {
            okays.add(decoder.getFirstObject(true).getInteger().intValue());
        }
        return new ProjectOK(okays);
    }
}
