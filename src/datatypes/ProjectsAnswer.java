package datatypes;

import asn1objects.ASN1Project;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.ddp2p.ASN1.ASNObj;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by armin1215 on 4/3/16.
 */
public class ProjectsAnswer {
    private List<Project> _projects;


    public ProjectsAnswer() {
        _projects = new LinkedList<>();
    }
    public ProjectsAnswer(List<Project> projects) {
        _projects = projects;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OKProjects:" + _projects.size() + ";");

        for (Project p : _projects) {
            sb.append(p.toString());
        }
        return sb.toString();
    }
    public void addProject(Project p) {
        _projects.add(p);
    }

    public List<Project> getProjects() {
        return _projects;
    }
}
