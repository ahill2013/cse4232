package asn1objects;


import datatypes.Project;
import datatypes.Task;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.ASNObj;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by armin1215 on 3/31/16.
 */
public class ASN1Project extends ASNObj {
    public static final int TAGVALUE = Encoder.TAG_SEQUENCE + 2;
    private Project _project;

    @Override
    public ASN1Project instance() {
        return new ASN1Project(" ", null);
    }

    public ASN1Project() {
        this("Empty", null);
    }
    public ASN1Project(Project p) {
        _project = p;
    }
    public ASN1Project(String name, List<Task> tasks) {
        _project = new Project(name, tasks);
    }

    public String toString() {
        return _project.toString();
    }

    @Override
    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();
        enc.addToSequence(new Encoder(_project.getName()).setASN1Type(Encoder.TAG_UTF8String));

        for (Task t : _project.getTasks()) {
            enc.addToSequence(new ASN1Task(t).getEncoder().setASN1Type(Encoder.TAG_SEQUENCE));
        }
        return enc.setASN1Type(Encoder.CLASS_APPLICATION, Encoder.PC_CONSTRUCTED, (byte) (Encoder.TAG_SEQUENCE + 2));
    }

    @Override
    public Project decode(Decoder dec) throws ASN1DecoderFail {

        String name;
        List<Task> tasks = new LinkedList<Task>();
        Decoder decoder = dec.getContent();
        name = decoder.getFirstObject(true).getString(Encoder.TAG_UTF8String);
        while (!decoder.isEmptyContainer()) {
            tasks.add(new ASN1Task().decode(decoder.getFirstObject(true)));
        }
        return new Project(name, tasks);
    }
}
