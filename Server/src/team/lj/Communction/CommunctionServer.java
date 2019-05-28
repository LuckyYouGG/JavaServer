package team.lj.Communction;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AcceptPendingException;
import java.util.*;

public class CommunctionServer {
     ServerSocket serverSocket;
     List<Socket> socketList = new ArrayList<>();
     LinkedList<Msginfo> msginfos = new LinkedList<Msginfo>();
     Map<String,Socket> map = new HashMap<String, Socket>();

//     public static void main(String args[]){ new CommunctionServer(); }
     public CommunctionServer()
     {
         try
         {
             serverSocket  = new ServerSocket(8888);
             System.out.println("服务器开启");
         }catch (IOException e){
             e.printStackTrace();
         }
         new AcceptSocketThread().start();
         new SendMSgTOClient().start();
     }
     class AcceptSocketThread extends Thread
     {
         public void run(){
             System.out.println("接受客户端连接");
             while(this.isAlive()){
                 try{
                     Socket socket = serverSocket.accept();
                     if(socket != null){
                         socketList.add(socket);
                         new GetMsgFromClient(socket).start();//开启新线程监视这个输入流
                     }
                 }catch (IOException e){
                     e.printStackTrace();
                 }
             }
         }
     }

     class GetMsgFromClient extends Thread
     {
        Socket socket;
        public GetMsgFromClient(Socket socket)
        {
            this.socket = socket;
        }
        public  void run()
        {
            while(!socket.isClosed())
            {
                System.out.println("1");
                try
                {
                    ObjectInputStream oss = new ObjectInputStream(socket.getInputStream());
                    Msginfo msginfo_tmp = (Msginfo) oss.readObject();
                    //System.out.println(msginfo_tmp.getStyledDocument().getLength()+"#");
                    if(msginfo_tmp != null)
                    {
                        if(msginfo_tmp.getMark() == 1)
                        {
                            map.put(msginfo_tmp.getSender(),socket);

                            System.out.println(msginfo_tmp.getSender());
                        }else
                            if(msginfo_tmp.getMark() == 5)
                            {
                                socket.close();
                            }
                            else
                            msginfos.add(msginfo_tmp);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
     class SendMSgTOClient extends Thread
     {
         public  void run()
         {
             while (this.isAlive())
             {
                 try
                 {
                     if(!msginfos.isEmpty())
                     {
                         Msginfo msg = msginfos.removeLast();
                       //  System.out.println(msg.getStyledDocument().getLength()+"$");
                         if (msg.getMark() == 4)
                         {
                             System.out.println("sdfgadsdg");
                              Msginfo msginfo = new Msginfo(4);
                              List<String> list = new ArrayList<>();
                             for (String key : map.keySet())
                             if(!map.get(key).isClosed())
                             {
                                 list.add(key);
                                 System.out.println("Key = " + key);
                             }else
                                 map.remove(key);
                             msginfo.setOnlines(list);
                             ObjectOutputStream oss = new ObjectOutputStream(map.get(msg.getRecer()).getOutputStream());
                             oss.writeObject(msginfo);
                             oss.flush();
                         }
                         if(msg.getMark() == 2)//私聊
                         {
                             if(map.get(msg.getRecer()).isClosed()) {
                                 map.remove(msg.getRecer());
                                 continue;
                             }
                             ObjectOutputStream oss = new ObjectOutputStream(map.get(msg.getRecer()).getOutputStream());
                             System.out.println(msg.getRecer()+"&");
                             oss.writeObject(msg);
                             oss.flush();
                         }
                         if(msg.getMark() == 3)//群聊
                         {
                             int length = socketList.size();
                             for(int i = 0;i < length; i++)
                               if(!socketList.get(i).isClosed())
                               {
                                 ObjectOutputStream oss = new ObjectOutputStream(socketList.get(i).getOutputStream());
                                 oss.writeObject(msg);
                                 oss.flush();
                               }else socketList.remove(i);
                         }
                     }
                 }catch (Exception e)
                 {
                     e.printStackTrace();
                 }
             }
         }
     }
}
