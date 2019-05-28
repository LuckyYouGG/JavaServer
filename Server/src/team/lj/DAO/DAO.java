package team.lj.DAO;

import team.lj.Log.LogInfo;
import team.lj.PersonalInfo.PersonalInfo;
import team.lj.Vote.VoteInfo;

import  java.sql.*;
import java.util.List;

public class DAO {

    private static Connection con;
    protected static String driver = "com.mysql.cj.jdbc.Driver";
    protected static String url = "jdbc:mysql://localhost:3306/班级管理系统?serverTimezone=GMT%2B8";
    protected static String user = "root";
    protected static String psw = "1107";
    protected static Statement statement;

    private DAO()
    {
        try
        {
            if(con == null)
            {
                Class.forName(driver);
                con = DriverManager.getConnection(url,user,psw);
                System.out.println("数据库连接成功");
            }else return;
        }catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("数据库连接失败");
        }
    }
    //添加投票
    public static int addVote(VoteInfo voteInfo)
    {
        String sql = "insert into toupiao(sender,time,title";
        List<String> options = voteInfo.getOptions();
        List<String> nums = voteInfo.getNums();
        int length = options.size();
        for(int i = 1;i <= length; i++)
        {
            sql = sql + ",option" + i;
        }
        sql += ",maxSelected";
        sql = sql + ") values('" + voteInfo.getSender() + "','" + voteInfo.getTime() +"','" + voteInfo.getTitle();
        for(int i = 0;i < length; i++)
        {
            sql = sql +"','" + options.get(i);
        }
        sql = sql + "','" + voteInfo.getMaxSelected();
        sql = sql + "')";

        System.out.println(sql);
        try
        {
            if(con == null) new DAO();
            con.createStatement().executeUpdate(sql);//!!!!!
            String sqlVote = "insert into vote(vote1,vote2,vote3,vote4,vote5,vote6,vote7,vote8) values" +
                    "('0','0','0','0','0','0','0','0')";
            con.createStatement().executeUpdate(sqlVote);
            addLog(voteInfo.getSender(),voteInfo.getTime(),"发布了一个标题为: "+voteInfo.getTitle()+"的投票");
            return 1;
        }catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    //得到全部的投票
    public static VoteInfo getAllVotes()
    {
        VoteInfo voteInfo = new VoteInfo();
        String sql1 = "select * from toupiao";
        String sql2 = "select * from vote";
        try
        {
            if(con == null) new DAO();
            ResultSet resultset1 = con.createStatement().executeQuery(sql1);
            ResultSet resultSet2 = con.createStatement().executeQuery(sql2);
            while(resultset1.next() && resultSet2.next())
            {
                VoteInfo tmp = new VoteInfo();
                tmp.setSender(resultset1.getString("sender"));
                tmp.setTime(resultset1.getString("time"));
                tmp.setTitle(resultset1.getString("title"));
                int node = 5;
                while(node <= 12 && resultset1.getString(node) != null)
                {
                    System.out.println(resultset1.getString(node)+"  "+resultSet2.getString(node - 3));
                    tmp.getOptions().add(resultset1.getString(node));
                    tmp.getNums().add(resultSet2.getString(node - 3));

                    node++;
                }
                tmp.setEverView(resultset1.getString("everView"));
                tmp.setRank(Integer.parseInt(resultset1.getString("num")));
                tmp.setMaxSelected(Integer.parseInt(resultset1.getString("maxSelected")));
                voteInfo.getVoteInfos().add(tmp);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return voteInfo;
    }
    //更新投票 rank 所选的人票数+1  在everView 加一个字段
    //第一行为1
    public static int updataVote(VoteInfo voteInfo)
    {
         int rank = voteInfo.getRank();
         String sender = voteInfo.getSender();
         List<String> nums = voteInfo.getNums();
         //int length = nums.size();
        try
        {
            System.out.println(rank);
            String sql = "select everView from toupiao where num='"+rank +"'";
            if(con == null) new DAO();
            ResultSet resultSet = con.createStatement().executeQuery(sql);
            resultSet.next();
            String everView = resultSet.getString("everView");
            if(everView == null) everView = "";
            everView = everView + sender;
            String wc = "update toupiao set everView='"+everView+"'" + "where num='"+rank+"'";
            con.createStatement().executeUpdate(wc);
            //上面个更新everView
            //下面更新票数
            String sql1 = "select * from vote where num='" + rank +"'";
            if(con == null) new DAO();
            ResultSet resultSet1 = con.createStatement().executeQuery(sql1);
            resultSet1.next();
            for(int i = 0;i < voteInfo.getNums().size(); i++)
            {
                if(nums.get(i) == null) break;
                int  tmp = Integer.parseInt(nums.get(i)) + Integer.parseInt(resultSet1.getString("vote"+(i+1)));
                String sql2 = "update vote set vote" +(i+1)+"='" + tmp+"'";
                con.createStatement().executeUpdate(sql2);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        return 1;
       // UPDATE table_name SET col-name='  '  ,[ col2_name =' ', ]    [WHERE id =' '  ]      //方括号为可选项     没有where就会更新所有行的
    }
    //检查用户名密码
    public static int isexist(String name,String psw)
    {
        String sql = "select * from info_everyone where name1='" +name+ "' and " + "psw='"+psw+"'";
        System.out.println(sql);
        try
        {
            if(con == null) new DAO();
         //   con.createStatement().executeUpdate(sql);
            ResultSet resultSet = con.createStatement().executeQuery(sql);

            if(resultSet.next())
            {
                System.out.println("用户账号密码正确");
                return 1;
            }else
            {
                System.out.println("用户账号密码错误");
                return -1;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    //用户注册
    public static int addInfo(String StudentId,String name ,String isNB,String from,String psw){
        String sql = "insert into info_everyone(StudentID,name1,isNB,from1,psw) values";
        sql = sql + "('" + StudentId + "','" +  name + "','" + isNB + "','" + from + "','"  +psw + "')";
        System.out.println(sql);

        try
        {
            if(con == null) new DAO();
            con.createStatement().executeUpdate(sql);
            return 1;
        }catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    public static int changeInfo(PersonalInfo tmp){
        String sql  = "update info_everyone set from1='" +
                tmp.getFromWhere()+"'," +"birthDay='"+tmp.getBirthDate()+"',"+"xingzuo='" +
                tmp.getConstellation()+"'," +"gxqm='"+tmp.getPersonalizedSignature()+"'," + "psw='" +tmp.getPsw()+"'" +"where name1='" + tmp.getName() +"'";
        System.out.println(sql+"^^^^^");
        try
        {
            if(con == null) new DAO();
            con.createStatement().executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 1;
    }
    public static PersonalInfo getInfo(String name)
    {
        PersonalInfo personalInfo = new PersonalInfo();
        String sql = "select * from info_everyone  where name1='" + name+"'";
        try
        {
            if(con == null) new DAO();
            ResultSet  resultSet = con.createStatement().executeQuery(sql);
            resultSet.next();

            personalInfo.setStudentID(resultSet.getString("StudentID"));
            personalInfo.setName(resultSet.getString("name1"));
            personalInfo.setIsNB(resultSet.getString("isNB"));
            personalInfo.setFromWhere(resultSet.getString("from1"));
            personalInfo.setBirthDate(resultSet.getString("birthDay"));
            personalInfo.setConstellation(resultSet.getString("xingzuo"));
            personalInfo.setPersonalizedSignature(resultSet.getString("gxqm"));
            personalInfo.setPsw(resultSet.getString("psw"));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return  personalInfo;
    }

    //公告操作
    public  static int addNotice(String name,String date,String title,String content)
    {

        String sql = "insert into notice(name,date,title,content) values";
        sql = sql +"('" + name + "','" +  date + "','" + title + "','"+ content + "')";
        System.out.println(name + date +title +content);
        try
        {
            if(con == null) new DAO();
            con.createStatement().executeUpdate(sql);
            addLog(name,date,"发表了一篇标题为: "+title+" 的公告");
            return 1;
        }catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    public static ResultSet getAllNotice()
    {
        if(con == null) new DAO();
        String sql = "select * from notice";
        ResultSet  resultSet = null;
        try
        {
            resultSet = con.createStatement().executeQuery(sql);
     //       resultSet.next();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultSet;
    }
    //添加日志记录
    public static int addLog(String name, String time,String done)
    {
        String sql = "insert into log(who,time,done) values";
        sql = sql + "('" + name +"','" +time+"','" + done +"')";
        try
        {
            if(con == null) new DAO();
            con.createStatement().executeUpdate(sql);
            return 1;
        }catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    //获得日志记录
    public static LogInfo getLog()
    {
        String sql = "select * from log";
        ResultSet  resultSet = null;
        LogInfo logInfo = new LogInfo();
        try
        {

            if(con == null) new DAO();
            resultSet = con.createStatement().executeQuery(sql);

            while (resultSet.next())
            {
                LogInfo tmp = new LogInfo();
                tmp.setName(resultSet.getString("who"));
                tmp.setDone(resultSet.getString("done"));
                tmp.setTime(resultSet.getString("time"));
                logInfo.getList().add(tmp);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return logInfo;
    }

}



















