package team.lj.Vote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VoteInfo implements Serializable
{
    private static final long serialVersionUID = -688679447042310L;
    /**
     * 1 发布投票
     * 2 得到全部历史投票
     * 3 投票（选了谁）
     */
    private int doWhat = 0;
    private  int rank ;//用这个就可以找到对应的 票数+1
    private String sender;
    private String time;
    private String title;
    private String everView;
    private List<String> options = new ArrayList<>();//选项的数量
    private List<String> nums = new ArrayList<>();//每个人对应的数量
    private List<VoteInfo> voteInfos = new ArrayList<>();
    private int maxSelected;

    public List<VoteInfo> getVoteInfos() { return voteInfos; }

    public void setVoteInfos(List<VoteInfo> voteInfos) { this.voteInfos = voteInfos; }

    public void setRank(int rank) { this.rank = rank; }
    public int getRank() { return rank; }

    public void setEverView(String everView) { this.everView = everView; }

    public String getEverView() { return everView; }

    public void setDoWhat(int doWhat) { this.doWhat = doWhat; }

    public int getDoWhat() { return doWhat; }

    public void setMaxSelected(int maxSelected) { this.maxSelected = maxSelected; }

    public int getMaxSelected() { return maxSelected; }

    public void setNums(List<String> nums) { this.nums = nums; }

    public List<String> getNums() { return nums; }

    public String getSender() { return sender; }

    public void setSender(String sender) { this.sender = sender; }

    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }

    public String getTitle() { return title; }

    public void setTime(String time) { this.time = time; }

    public List<String> getOptions() { return options; }

    public void setOptions(List<String> options) { this.options = options;  }
}
