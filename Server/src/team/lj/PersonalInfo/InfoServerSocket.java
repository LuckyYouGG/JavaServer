package team.lj.PersonalInfo;

import team.lj.DAO.DAO;

import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class InfoServerSocket {
    ServerSocket serverSocket;
    public InfoServerSocket()
    {
        try
        {
            serverSocket = new ServerSocket(9999);
            new getSocket().start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //public static void main(String args[]){ new InfoServerSocket(); }
    class getSocket extends Thread
    {
        @Override
        public void run()
        {
            super.run();
            while(this.isAlive())
            {
                try
                {
                    Socket socket = serverSocket.accept();
                    if(socket != null){
                        new checkSocket(socket).start();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    class checkSocket extends Thread
    {
        Socket socket;
        public checkSocket(Socket socket)
        {
            this.socket = socket;
        }
        @Override
        public void run()
        {
            super.run();
            while(2 > 1)
            {
                try
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Object object = objectInputStream.readObject();
                    if(object != null)
                    {
                        PersonalInfo personalInfo  = (PersonalInfo)object;
                        if(personalInfo.getDoWhat() == 1)
                        {
                            int result = addInfo(personalInfo);
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bufferedWriter.write("注册成功");
                            bufferedWriter.flush();
                        }
                        if(personalInfo.getDoWhat() == 2)//变更某个人的信息
                        {
                            changeInfo(personalInfo);
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bufferedWriter.write("信息更改成功");
                            bufferedWriter.flush();
                        }
                        if(personalInfo.getDoWhat() == 3)//获得某个人的信息
                        {
                            PersonalInfo personalInfo1 =  getInfo(personalInfo);
                            System.out.println(personalInfo1.getName()+"%%%");
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeObject(personalInfo1);
                            objectOutputStream.flush();
                        }
                        if(personalInfo.getDoWhat() ==4)//登陆检查
                        {
                           if(checkInfo(personalInfo) == 1)
                           {
                               BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                               bufferedWriter.write("账号密码正确");
                               bufferedWriter.flush();
                           }else
                           {
                               BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                               bufferedWriter.write("账号密码不正确");
                               bufferedWriter.flush();
                           }
                        }
                        socket.shutdownInput();
                        socket.shutdownOutput();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    public synchronized int  addInfo(PersonalInfo personalInfo)
    {
        int result = 0;
        result = DAO.addInfo(personalInfo.getStudentID(),personalInfo.getName(),
                    personalInfo.getIsNB(),personalInfo.getFromWhere(),personalInfo.getPsw());

        return result;
    }
    public synchronized int  checkInfo(PersonalInfo personalInfo)//登录信息检查
    {
        return DAO.isexist(personalInfo.getName(),personalInfo.getPsw());
    }
    public synchronized void changeInfo(PersonalInfo personalInfo)
    {
         DAO.changeInfo(personalInfo);
    }
    public synchronized PersonalInfo getInfo(PersonalInfo personalInfo)
    {
      PersonalInfo personalInfo1 = DAO.getInfo(personalInfo.getName());
      return personalInfo1;//这里是xxx1
    }

}

















