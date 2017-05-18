package com.jolytech.sample.dreamtalk;

import android.app.Application;
import android.os.Handler;
import android.support.design.widget.TabLayout;

import java.util.ArrayList;

public class MyGlobals extends Application {
    private boolean readyForUpdates=false;
    public boolean GetReadyForUpdates(){return readyForUpdates;};
    public void SetReadyForUpdates(boolean ready){readyForUpdates=ready;};
    private String useridOfChatPeer;

    Handler handlermain;
    public void setHandlerMain(Handler hd){handlermain=hd;}
    public Handler getHandlerMain(){return handlermain;}

    Handler handlerIncommingCallActivity;
    public void setHandlerIncommingCallActivity(Handler hd){handlerIncommingCallActivity=hd;}
    public Handler getHandlerIncommingCallActivity(){return handlerIncommingCallActivity;}

    Handler handlerOutgoingCallActivity;
    public void setHandlerOutgoingCallActivity(Handler hd){handlerOutgoingCallActivity=hd;}
    public Handler getHandlerOutgoingCallActivity(){return handlerOutgoingCallActivity;}

    boolean udpholepunch=false;
    private ArrayList<MyContact> MyContacts = new ArrayList<MyContact>();
    private int cnt=0;
    private TabLayout.Tab tab;

    private String  chatusers;
    public String GetChatUsers(){return chatusers;};
    public void   SetChatUsers(String  _chatusers){chatusers=_chatusers;};

    private ArrayList<String> PeerRequests=new ArrayList<String>();
    public ArrayList<String> GetPeerRequests(){return PeerRequests;};
    public void SetPeerRequests(ArrayList<String> peerReq){PeerRequests=peerReq;};
    public TabLayout.Tab GetTab(){return tab;};
    public void          SetTab(TabLayout.Tab tb){tab=tb;};
    public ArrayList<MyContact> GetContacts() {
        return MyContacts;
    }
    public String GetUserIdOfChatPeer() {return useridOfChatPeer;}
    public int getCnt() {return cnt;}
    public void SetUserIdOfChatPeer(String _useridOfChatPeer) {useridOfChatPeer=_useridOfChatPeer;}

    public void setCnt(int cntv) {
        cnt = cntv;
    }
    public void SetUdpHolePunch(boolean val){udpholepunch=val;}

    //this is boolean that is true when voice conversation is on, false otherwise
    boolean talking=false;
    public boolean GetTalking(){return talking;};
    public void   SetTalking(boolean _talking){talking=_talking;};

    boolean taskInBacground=false;
    public boolean IsTaskInBaground(){return taskInBacground;};
    public void SetTaskInBaground(boolean val){taskInBacground=val;}

}
