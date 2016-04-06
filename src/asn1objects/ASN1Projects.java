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
    public static final int TAGVALUE = Encoder.TAG_SEQUENCE + 4;

    private Projects projectNames;
    public ASN1Projects() {
        projectNames = new Projects();
    }

    public ASN1Projects(Projects pNames) {
        projectNames = pNames;
    }

    @Override
    public ASN1Projects instance() {
        return new ASN1Projects();
    }

    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();

        for (String projectName : projectNames.getProjectNames()) {
            enc.addToSequence(new Encoder(projectName).setASN1Type(Encoder.TAG_UTF8String));
        }

        return enc.setASN1Type(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, (byte) TAGVALUE);
    }

    @Override
    public Projects decode(Decoder dec) throws ASN1DecoderFail {
        Decoder decoder = dec.getContent();

        Projects pNames = new Projects();
        while (!decoder.isEmptyContainer()) {
            pNames.addProjectName(decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String));
        }

        return pNames;
    }
}
