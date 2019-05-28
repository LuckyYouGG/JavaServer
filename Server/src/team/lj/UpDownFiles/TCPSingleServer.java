package team.lj.UpDownFiles;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPSingleServer
{
    private static FileUtils fileUtils = new FileUtils();

    private static FileSetting fileSetting = new FileSetting();

    private List<Socket> socketsList = new ArrayList<Socket>();

    private String pathName = "E:\\sharefiles";

    ServerSocket serverSocket;

//    public static void main(String args[]){ new TCPSingleServer(); }
    public TCPSingleServer()
    {
        try
            {
             serverSocket = new ServerSocket(6666);
            }catch (Exception e)
            {
              e.printStackTrace();
            }
        new AcceptSockek().start();
    }

    class  AcceptSockek extends Thread
    {
        @Override
        public void run()
        {
            super.run();
            System.out.println("服务器启动，等待客户端的连接。。。");
            while (this.isAlive())
            {
                try
                {
                    Socket socket = serverSocket.accept();
                        if(socket != null)
                        {
                           // socketsList.add(socket);
                            new checkSocket(socket).start();

                            System.out.println("有客户端连接");
                        }
                   }catch (Exception e){
                      e.printStackTrace();
                }
            }
        }
    }
    //判断这个套接字传过来的对象是要获得啥

    class checkSocket extends Thread
    {
        Socket socket;
        Object obj = null;
        public checkSocket(Socket socket){this.socket = socket;}

        public void run()
        {
            while(1 != 2)
            {
                System.out.println("wcnm");
                super.run();
                try
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    obj = objectInputStream.readObject();
                    if(obj != null)
                    {
                        fileSetting = (FileSetting)obj;
                        int doWhat = fileSetting.getDoWhat();

                        if(doWhat == 1)//表示获得文件列表
                        {
                            System.out.println("%%");
                            findFiles(socket);
                        }
                        if(doWhat == 2)//上传文件
                        {
                            getFile(fileSetting);
                            socket.shutdownInput();
                            socket.shutdownOutput();
                        }
                        if(doWhat == 3)
                        {
                            System.out.println("有用户要下载文件了");
                            sendFile(socket,fileSetting);
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
           // this.stop();
        }
    }
    //这里可能多个线程同时用
    //用来返回服务器列表
    public synchronized void findFiles(Socket socket)
    {
        List<String> list_filesname = new ArrayList<String>();
        File file = new File(pathName);
        File[] array = file.listFiles();
        int l = array.length;
        for(int i = 0; i < l; i++)
        {
            if(array[i].isFile())
            {
                list_filesname.add(array[i].getName());
            }
        }
        FileSetting fileSetting = new FileSetting();
        fileSetting.setFilesNameList(list_filesname);
        fileSetting.setDoWhat(1);
        try
        {
            socket.shutdownInput();
                ObjectOutputStream  objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(fileSetting);
                objectOutputStream.flush();//输出流用完之后flush
            socket.shutdownOutput();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public synchronized void sendFile(Socket socket,FileSetting fileSetting)
    {
        FileSetting tmp = new FileSetting();
        tmp.setDoWhat(3);//对应于客户端的下载文件
        List<String> filesName = fileSetting.getFilesNameList();
        tmp.setFilesNameList(filesName);

        int l = filesName.size();
        for(int i = 0;i < l; i++){
          tmp.getStreams().add(fileUtils.encryptToBase64(pathName+"\\"+filesName.get(i)));
        }
        try{
            socket.shutdownInput();
                  ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                  objectOutputStream.writeObject(tmp);
                  objectOutputStream.flush();
            socket.shutdownOutput();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 对应于客户端的上传文件
     * @param fileSetting
     */
    public synchronized void getFile(FileSetting fileSetting)
    {
        File dir = new File(pathName);//打开目录
        if (!dir.exists())
        {
            dir.mkdir();
        }

        /**
         * 获取base64流转为指定文件
         */
        List<String> streams = fileSetting.getStreams();
        int l = streams.size();
        for(int i = 0; i < l; i++)
        {
            File file = new File(dir, fileSetting.getFilesNameList().get(i));//在目录下面新建一个“假”文件
            String stream = streams.get(i);
            fileUtils.decryptByBase64(stream, file.getPath());//将内容写进去
        }
    }
}










