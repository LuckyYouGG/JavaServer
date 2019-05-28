package team.lj.PersonalInfo;

import java.io.Serializable;
import java.net.ServerSocket;

public class PersonalInfo implements Serializable
{

    /**
     * 1 代表 注册信息
     * 2 代表 修改某个人的信息
     * 3 代表 获取某个人的信息
     * 4 代表 注册登陆验证
     */
    private static final long serialVersionUID = -68497943454667711L;
    private int doWhat = 0;
    public void setDoWhat(int doWhat) { this.doWhat = doWhat; }
    public int getDoWhat() { return doWhat; }

    private    String StudentID = "";
    public String getStudentID() { return StudentID; }
    public void setStudentID(String studentID) { StudentID = studentID;}



    private  String name = "";
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    private  String isNB = "";
    public String getIsNB() { return isNB; }
    public void setIsNB(String isNB) { this.isNB = isNB; }


    private String fromWhere = "";
    public void setFromWhere(String fromWhere) { this.fromWhere = fromWhere; }
    public String getFromWhere() { return fromWhere; }

    private String psw = "";
    public void setPsw(String psw) { this.psw = psw; }
    public String getPsw() { return psw; }

    private  String birthDate = "";
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getBirthDate() { return birthDate; }

    private    String constellation = "";
    public void setConstellation(String constellation) { this.constellation = constellation; }
    public String getConstellation() { return constellation; }



    private    String PersonalizedSignature = "";
    public void setPersonalizedSignature(String personalizedSignature) { PersonalizedSignature = personalizedSignature; }
    public String getPersonalizedSignature() { return PersonalizedSignature; }
}




















