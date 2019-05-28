package team.lj;

import team.lj.Communction.CommunctionServer;
import team.lj.Log.LogServer;
import team.lj.Notice.NoticeServer;
import team.lj.PersonalInfo.InfoServerSocket;
import team.lj.UpDownFiles.TCPSingleServer;
import team.lj.Vote.VoteServerSocket;

public class Main{
    public static void main(String args[])
    {
        new VoteServerSocket();
        new TCPSingleServer();
        new InfoServerSocket();
        new NoticeServer();
        new CommunctionServer();
        new LogServer();
    }
}
