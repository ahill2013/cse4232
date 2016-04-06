package asn1objects;

import datatypes.GetProject;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

/**
 * Created by armin1215 on 4/5/16.
 */
public class ASN1GetProject extends ASNObj {
    public static final int TAGVALUE = Encoder.TAG_SEQUENCE + 6;
    private GetProject _getProject;

    public ASN1GetProject() {}
    public ASN1GetProject(GetProject gp) {
        _getProject = gp;
    }



    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder(_getProject.getName());
        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_PRIMITIVE, (byte) TAGVALUE);
    }

    @Override
    public GetProject decode(Decoder dec) throws ASN1DecoderFail {
        return new GetProject(dec.getFirstObject(true).getString(Encoder.TAG_UTF8String));

    }
}
