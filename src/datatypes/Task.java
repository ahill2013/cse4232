package datatypes;

import java.util.Date;

/**
 * Created by armin1215 on 3/31/16.
 */
public class Task {
    private String name;
    private Date start;
    private Date end;
    private String ip;
    private int port;
    private boolean status;

    public String toString() {
        final StringBuilder task = new StringBuilder();
        task.append(name + "\n");
        task.append("\t\t" + start + "\n");
        task.append("\t\t" + end + "\n");
        task.append("\t\t" + ip + "\n");
        task.append("\t\t" + port + "\n");
        task.append("\t\t" + status + "\n");
        return task.toString();
    }

    public Task(String name, Date start, Date end, String ip, int port, boolean status) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.ip = ip;
        this.port = port;
        this.status = status;
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