package asn1objects;

import datatypes.Task;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by armin1215 on 4/5/16.
 */
public class DatatypeDecoder {

    DatatypeDecoder() {}
    ASN1Task task = new ASN1Task();
    ASN1Project project = new ASN1Project();
    ASN1Projects projects = new ASN1Projects();
    ASN1ProjectsAnswer projectsAnswer = new ASN1ProjectsAnswer();
    ASN1ProjectOK projectOK = new ASN1ProjectOK();



    public List<Object> decodeMessage(Decoder dec) throws ASN1DecoderFail {
        List<Object> decoded = new LinkedList<>();

        while (!dec.isEmptyContainer()) {
            switch(dec.tagVal()) {
                case ASN1Task.TAGVALUE:
                    decoded.add(task.decode(dec.getFirstObject(true)));
                    break;
                case ASN1Project.TAGVALUE:
                    break;
                case ASN1Projects.TAGVALUE:
                    break;
                case ASN1ProjectOK.TAGVALUE:
                    break;
                default:
                    throw new ASN1DecoderFail("Invalid ASN1 Tag Value");
            }
        }
        return decoded.toString();
    }

}
