package team.lj.Vote;

import team.lj.DAO.DAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class VoteServerSocket {
    ServerSocket serverSocket;
 //   public static void  main(String args[]){ new VoteServerSocket(); }
    public VoteServerSocket()
    {
        try
        {
            serverSocket = new ServerSocket(5555);
            new getSocket().start();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    class getSocket extends Thread
    {
        @Override
        public void run()
        {
            while (this.isAlive())
            {
                super.run();
                try
                {
                    Socket socket = serverSocket.accept();
                    if(socket != null)
                    {
                        new getMessage(socket).start();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
    class getMessage extends Thread
    {
        Socket socket;
        public getMessage(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public void run()
        {
            super.run();
            while (2 > 1)
            {
                try
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Object object = null;
                    object = objectInputStream.readObject();
                    if(object != null)
                    {
                        socket.shutdownInput();

                        VoteInfo voteInfo = (VoteInfo) object;
                        if(voteInfo.getDoWhat() == 1)//发布投票，需要插入到数据库里
                        {
                            DAO.addVote(voteInfo);
                            try{
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                bufferedWriter.write("发布成功");
                                bufferedWriter.flush();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        if(voteInfo.getDoWhat() == 2)//得到全部历史投票，从数据库里面弄出来
                        {
                            VoteInfo tmp = DAO.getAllVotes();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeObject(tmp);
                            objectOutputStream.flush();
                        }
                        if(voteInfo.getDoWhat() == 3)//投票结果，需要插入到数据库里
                        {
                            DAO.updataVote(voteInfo);
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bufferedWriter.write("投票成功");
                            bufferedWriter.flush();
                        }
                        socket.shutdownOutput();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}





















