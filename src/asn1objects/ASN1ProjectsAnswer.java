package asn1objects;

import datatypes.Project;
import datatypes.Projects;
import datatypes.ProjectsAnswer;
import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import net.ddp2p.ASN1.Encoder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by armin1215 on 3/31/16.
 */
public class ASN1ProjectsAnswer {
    private ProjectsAnswer _projectAns;

    public ASN1ProjectsAnswer(ProjectsAnswer p) {
        _projectAns = p;
    }

    public ASN1ProjectsAnswer() {
        _projectAns = new ProjectsAnswer(null);
    }

    public ASN1ProjectsAnswer instance() {
        return new ASN1ProjectsAnswer();
    }

    public Encoder getEncoder() {
        Encoder enc = new Encoder().initSequence();

        for (Project p : _projectAns.getProjects()) {
            enc.addToSequence(new ASN1Project(p).getEncoder().setASN1Type(Encoder.TAG_SEQUENCE));
        }
        return enc.setASN1Type(Encoder.CLASS_PRIVATE, Encoder.PC_CONSTRUCTED, Encoder.TAG_SEQUENCE);
    }

    public ProjectsAnswer decode(Decoder dec) throws ASN1DecoderFail {
        List<Project> projects = new LinkedList<>();
        Decoder decoder = dec.getContent();

        while(!decoder.isEmptyContainer()) {
            projects.add(new ASN1Project().decode(decoder.getFirstObject(true)));
        }

        return new ProjectsAnswer(projects);
    }
}
