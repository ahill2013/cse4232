package datatypes;

import net.ddp2p.ASN1.ASNObj;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract version of ASN1Take. Part of projects and encoded in sequence inside
 * a project.
 */
public class Task {
    private String name;
    private Date start;
    private Date end;
    private String ip;
    private int port;
    private boolean status;

    private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd:hh'h'mm's'ss's'SSS'Z'");

    public Task(String name, Date start, Date end, String ip, int port, boolean status) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.ip = ip;
        this.port = port;
        this.status = status;
    }


    public String toString() {
        final StringBuilder task = new StringBuilder();
        task.append(name + ";");
        task.append(_sdf.format(start) + ";");
        task.append(_sdf.format(end) + ";");
        task.append(ip + ";");
        task.append(port + ";");
        task.append(status + ";");
        return task.toString();
    }

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return start;
    }

    public Date getEndTime() {
        return end;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean getStatus() {
        return status;
    }
}
