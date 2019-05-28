package team.lj.UpDownFiles;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileSetting implements Serializable
{

    /**
     * 文件流
     */
    private static final long serialVersionUID = -6849794470754667710L;

    private List<String> streams = new ArrayList<>();

    /**
     *  文件列表 可能是 想要下载的，可能是想要上传的，也可能是获取的服务器文件列表
     */

    private List<String> FilesNameList = new ArrayList<>();
    /**
     * 想要干啥
     */
    private int doWhat = 0;

    public void setFilesNameList(List<String> filesNameList) { FilesNameList = filesNameList; }

    public List<String> getFilesNameList() { return FilesNameList; }

    public void setDoWhat(int doWhat){ this.doWhat = doWhat; }

    public int getDoWhat(){ return doWhat; }



    public List<String> getStreams() {
        return streams;
    }

    public void setStreams(List<String> streams) {
        this.streams = streams;
    }
}
