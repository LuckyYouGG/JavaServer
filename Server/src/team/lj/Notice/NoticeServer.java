package team.lj.Notice;

import team.lj.DAO.DAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NoticeServer {
    ServerSocket serverSocket ;


    public NoticeServer()
    {
        connServerSocket();
        new getSocket().start();
    }

    public  void connServerSocket()
    {
        try {
            serverSocket = new ServerSocket(7777);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
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
                    new getNotice(socket).start();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    class getNotice extends  Thread
    {
        Socket socket;
        public getNotice(Socket socket)
        {
          this.socket = socket;
        }
        @Override
        public void run()
        {
            super.run();
            while(!socket.isClosed())
            {
                try
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Object object = objectInputStream.readObject();
                    if(object != null)
                    {
                        NoticeInfo notice = (NoticeInfo)object;
                        if(notice.getDoWhat() == 1)
                        {
                            System.out.println(notice.getSender());

                            int result = DAO.addNotice(notice.getSender(),notice.getData(),notice.getTitle(),notice.getContent());
                            System.out.println(notice.getSender());

                            if(result == 1)
                            {
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                bufferedWriter.write("发布成功");
                                bufferedWriter.flush();

                                socket.shutdownOutput();
                                socket.shutdownInput();
                                socket.close();
                            }
                        }
                        if(notice.getDoWhat() == 2)
                        {
                            ResultSet resultSet = DAO.getAllNotice();
                            if(resultSet.next() == false)
                            {

                            }
                            else
                            {
                                List<NoticeInfo> noticeList = new ArrayList<>();
                                while(resultSet.next())
                                {
                                    NoticeInfo tmp = new NoticeInfo();
                                    tmp.setSender(resultSet.getString(2));
                                    tmp.setDate(resultSet.getString(3));
                                    tmp.setTitle(resultSet.getString(4));
                                    tmp.setContent(resultSet.getString(5));
                                    noticeList.add(tmp);

                                    System.out.println("5");
                                }

                               NoticeInfo tmp = new NoticeInfo();
                               tmp.setDoWhat(2);
                               tmp.setNoticeList(noticeList);

                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                objectOutputStream.writeObject(tmp);
                                objectOutputStream.flush();
                                socket.shutdownInput();
                                socket.shutdownOutput();
                                socket.close();
                            }
                        }
                    }
                  //  objectInputStream.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

 //   public static void main(String args[]){ new NoticeServer(); }
}




















