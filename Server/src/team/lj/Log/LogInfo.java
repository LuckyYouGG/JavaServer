package team.lj.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogInfo implements Serializable {
    private static final long serialVersionUID = -68497945654667711L;
    private String name;
    private String time;
    private String done;
    private List<LogInfo> list = new ArrayList<>();

    public List<LogInfo> getList() { return list; }
    public void setList(List<LogInfo> list) { this.list = list; }

    public void setTime(String time) { this.time = time; }
    public String getTime() { return time; }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public String getDone() { return done; }
    public void setDone(String done) { this.done = done; }
}
