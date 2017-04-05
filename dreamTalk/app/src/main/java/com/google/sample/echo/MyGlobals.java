package com.google.sample.echo;

import android.app.Application;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.google.sample.echo.data.MyContact;

/**
 * Created by aj on 3/19/17.
 */
import com.google.sample.echo.data.MyContact;
public class MyGlobals extends Application {
    private String useridOfChatPeer;
    Handler handlermain;
    boolean udpholepunch=false;
    private ArrayList<MyContact> MyContacts = new ArrayList<MyContact>();
    private int cnt=0;
    private TabLayout.Tab tab;
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
    public void setHandlerMain(Handler hd){handlermain=hd;}
    public Handler getHandlerMain(){return handlermain;}
    public void setCnt(int cntv) {
        cnt = cntv;
    }
    public void SetUdpHolePunch(boolean val){udpholepunch=val;}

}
