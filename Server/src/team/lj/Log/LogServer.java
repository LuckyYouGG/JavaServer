package team.lj.Log;


import team.lj.DAO.DAO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LogServer {
    ServerSocket serverSocket;
    public LogServer()
    {
        try {
            serverSocket = new ServerSocket(10001);
            new getSocket().start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class getSocket extends Thread
    {
        @Override
        public void run() {
            super.run();
            while(this.isAlive())
            {
                try
                {
                    Socket socket = serverSocket.accept();
                    new getLog(socket).start();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
    class getLog extends Thread
    {
        Socket socket;
        public getLog(Socket socket)
        {
              this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            while(this.isAlive())
            {
                LogInfo logInfo = null;
                try
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    logInfo =(LogInfo) objectInputStream.readObject();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(logInfo != null)
                {

                    LogInfo tmp = DAO.getLog();
                    try
                    {   socket.shutdownInput();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(tmp);
                        objectOutputStream.flush();
                        socket.shutdownOutput();
                        socket.close();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}













