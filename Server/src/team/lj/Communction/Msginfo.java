package team.lj.Communction;

import javax.swing.text.StyledDocument;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Msginfo implements Serializable {
    private int mark = 0;
    /**
     * 1-->上传自己信息
     * 2-->私聊
     * 3-->群聊
     * 4-->返回在在线人数
     * 5-->关闭
     */

    private String sender;
    private String recer;

    private StyledDocument styledDocument = null;

    private String sendTime = "";
    private List<String> onlines = new ArrayList<String>();//存在线的人数

    public Msginfo(int doWhat){ mark = doWhat; }
    public int getMark() { return mark; }

    public void setMark(int mark) { this.mark = mark; }

    public void setStyledDocument(StyledDocument styledDocument){ this.styledDocument = styledDocument;}
    public StyledDocument getStyledDocument(){ return styledDocument; }
    public void setOnlines(List<String> onlines) { this.onlines = onlines; }
    public List<String> getOnlines() { return onlines; }

    public void setSendTime(String sendTime) { this.sendTime = sendTime; }
    public String getSendTime() { return sendTime; }

    public void setSender(String sender){ this.sender = sender; }
    public String getSender(){ return sender; }

    public void setRecer(String recer){ this.recer = recer; }

    public String getRecer() { return recer; }
}
