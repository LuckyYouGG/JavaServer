package team.lj.Notice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoticeInfo implements Serializable {

    private static final long serialVersionUID = -6849794345654667711L;

    //1是发公告
    //2是获得公告列表
    //3是删除公告
    private int doWhat = 0;
    private String sender ;
    private String date ;
    private String content;
    private String title;

    private List<NoticeInfo> noticeList = new ArrayList<>();

    public void setNoticeList(List<NoticeInfo> noticeList) { this.noticeList = noticeList; }

    public List<NoticeInfo> getNoticeList() { return noticeList; }
    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return title; }

    public void setDoWhat(int doWhat) { this.doWhat = doWhat; }

    public int getDoWhat() { return doWhat; }

    public void setSender(String sender) { this.sender = sender; }
    public String getSender() { return sender; }


    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setDate(String date) { this.date = date; }

    public String getData() { return date; }

    public NoticeInfo(){  }
}
