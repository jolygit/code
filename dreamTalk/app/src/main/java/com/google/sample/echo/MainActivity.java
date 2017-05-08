package com.google.sample.echo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import com.google.sample.echo.data.MyContact;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity  {
    String chatusers;// are peer user ids to whom the chat is dirrected
    String namesToShow="   ";
    registrationFragment rfragment;
    loginFragment lfragment;
    requestsArrayAdapter requestAdapter;
    ArrayAdapter<String> chatAdapter;
    ArrayAdapter<MyContact> contactsadapter;
    ListView chatList;
    ListView requestList;
    final static String token="zilimbekdipsheetmagamedovhasanjemalusho";////this is the token to separate the chat hist. It has to be unique so that user text is not mistaken for it, that is why comma could not be used!
    String login;
    String selfUid;
    ViewPager viewPager;
    PageAdapter pageadapter;
    TabLayout tabLayout;
    Toolbar toolbar;
    Boolean inChat=false;
    static {
        System.loadLibrary("echo");
    }
    public native int client();
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
        //bindService(new Intent(this, ServerConnectionService.class), mConnection,Context.BIND_AUTO_CREATE);

    }
    class ChatMainHandler extends Handler {
        private static final String TAG = "from handler:";

        public ChatMainHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundleForLoader = msg.getData();
            if(bundleForLoader.containsKey("toastMsg")) {
                String RequestError = bundleForLoader.getString("toastMsg");
                if (RequestError.length() > 0) {
                    //Toast.makeText(this, RequestError, Toast.LENGTH_SHORT).show();
                }
            }
            else if(bundleForLoader.containsKey("chat")){
                String RequestType = bundleForLoader.getString("chat");// chat:senderuserid:chatmsg
                String[] tokens=RequestType.split(":");
                String senderUserid=tokens[1];
                String chatmsg=tokens[2];
                AppendToChatScreen(senderUserid,chatmsg);

            }
            else if(bundleForLoader.containsKey("IncomingVoiceFrom")){
                String[] tokens=bundleForLoader.getString("IncomingVoiceFrom").split(":");
                String uid=tokens[1];
                ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
                String fullName="";
                for (int j = 0; j < MyContacts.size(); j++) {
                    if (MyContacts.get(j).getUsername().equals(uid)) {
                        fullName=MyContacts.get(j).getfirstName()+" "+MyContacts.get(j).getlastName();
                        break;
                    }
                }
                VoiceCallNotification(fullName,uid,"incomming");
            }
            else if(bundleForLoader.containsKey("HangupVoiceFrom")){
                ((MyGlobals) getApplicationContext()).SetTalking(false);
                Intent mainIntent = new Intent("android.intent.category.LAUNCHER");
                mainIntent.setClassName("com.google.sample.echo", "com.google.sample.echo.MainActivity");
                mainIntent.putExtra("hangup","");
                startActivity(mainIntent);//primary reason is to refresh the call hangup button
            }
            else if(bundleForLoader.containsKey("mainFragment") && inChat==false){
                ((MyGlobals) getApplicationContext()).SetReadyForUpdates(true);
                setContentView(R.layout.tmp);//start
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
                tabLayout.addTab(tabLayout.newTab().setText("Add Contacts"));
                viewPager = (ViewPager) findViewById(R.id.pager);
                pageadapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),getApplicationContext());
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                viewPager.setAdapter(pageadapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) { }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) { }
                });
                int numPeerRequest=((MyGlobals) getApplicationContext()).GetPeerRequests().size();
                View v=pageadapter.getTabView(numPeerRequest,getApplicationContext());
                tabLayout.getTabAt(1).setCustomView(v);
                ((MyGlobals) getApplicationContext()).SetTab(tabLayout.getTabAt(1));
                pageadapter.notifyDataSetChanged();
            }
            else if(bundleForLoader.containsKey("updateFragments") && inChat==false){
                int numPeerRequest=((MyGlobals) getApplicationContext()).GetPeerRequests().size();
                TextView cnt=(TextView)((MyGlobals) getApplicationContext()).GetTab().getCustomView().findViewById(R.id.count1);
                if(numPeerRequest>0) {
                    cnt.setText("" + numPeerRequest);
                    cnt.setBackgroundResource(R.drawable.circle);
                }
                else{
                    cnt.setText("");
                    cnt.setBackgroundResource(android.R.color.transparent);
                }
                pageadapter.notifyDataSetChanged();
            }
        }
    }
    public void onBackPressed() {
        if(inChat){
            inChat=false;
            setContentView(R.layout.tmp);//start
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
            tabLayout.addTab(tabLayout.newTab().setText("Add Contacts"));
            viewPager = (ViewPager) findViewById(R.id.pager);
            pageadapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),getApplicationContext());
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            viewPager.setAdapter(pageadapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}
                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }
                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });
            int numPeerRequest=((MyGlobals) getApplicationContext()).GetPeerRequests().size();
            View v=pageadapter.getTabView(numPeerRequest,getApplicationContext());
            tabLayout.getTabAt(1).setCustomView(v);
            pageadapter.notifyDataSetChanged();
        }
        else {
            finish();
        }
    }
    public void submit(View view){
        pageadapter.notifyDataSetChanged();
        EditText UserName = (EditText) findViewById(R.id.typeuserid);
        String unameSt = UserName.getText().toString();
        String request="invitefriend:"+unameSt+"\n";
        UserName.setText("");
        try {
            writeToStdin(request);//send request to client() in native code running on separete thread to terminate itself
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// this is called from the native client() function when chat text need to be passed to the main thread
    public void ProcessRequest(String str) {
        String[] resp = str.split(":");
        if(resp.length == 4 && resp[2].equals("invitefriend") && resp[3].equals("request failed because user does not exist. Try again.")){//
           // TextView UserName = (TextView) findViewById(R.id.submit_notify);
           // UserName.setText("User does not exit. Try again!");
            //UserName.clearFocus();
            //Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
        }
        else if(resp.length == 4 && resp[2].equals("requests")){
            String requests=resp[3];
            String[] usrs = requests.split(":");
            for(String usr:usrs) {
                ((MyGlobals) getApplicationContext()).GetPeerRequests().add(usr);
            }
            if(((MyGlobals) getApplicationContext()).GetReadyForUpdates()) {
                SendJobToHandler("updateFragments", str,"MainActivity");
            }
        }
        else if(resp.length == 4 && resp[2].equals("invitefriend") && resp[3].equals("request filed sucessfully")){
           // TextView UserName = (TextView) findViewById(R.id.submit_notify);
           // UserName.setText("Request submitted!");
           // UserName.clearFocus();
           // Toast.makeText(this,"request submitted!", Toast.LENGTH_SHORT).show();
        }
        else if (resp.length == 4 && resp[3].equals("ok")) {
            SharedPreferences.Editor editor = getSharedPreferences("PeerInfo", MODE_PRIVATE).edit();
            editor.putString("login",login);//save login info in sharedPrefs
            editor.commit();
            allfriends();
        } else if (resp.length >= 4 && resp[2].equals("allfriends")) {
            String frlistSt = resp[3].substring(0, resp[3].length());
            CreateAllFriendsList(frlistSt,true);
            onlinefriends();
        } else if (resp.length == 3 && resp[2].equals("allfriends")) {
            SendJobToHandler("mainFragment",str,"MainActivity");//after initial registration friends list is empty
        }
        else if (resp.length > 2 && resp[2].equals("onlinefriends")) {
            if (resp.length == 4) {
                String frlistSt = resp[3].substring(0, resp[3].length());
                CreateOnlineFriendsStatus(frlistSt);
            }
            else{
                CreateOnlineFriendsStatus("");
            }
            SendJobToHandler("mainFragment",str,"MainActivity");
        }
        else if((resp.length >=2 && resp[0].equals("chat"))){ // chat:<sourceUserName>:chatmsg
            SendJobToHandler("chat",str,"MainActivity");
        }
        else if(resp[0].equals("IncomingVoiceFrom")){
            SendJobToHandler("IncomingVoiceFrom",str,"MainActivity");
        }
        else if(resp[0].equals("HangupVoiceFrom")){
            SendJobToHandler("HangupVoiceFrom",str,"MainActivity");
        }
        else if(resp[0].equals("CanceledVoiceFrom")){
            SendJobToHandler("CanceledVoiceFrom",str,"incommingCallActivity");
        }
        else if(resp[0].equals("RejectedVoiceFrom")){
            SendJobToHandler("RejectedVoiceFrom",str,"outgoingCallActivity");
        }
        else if(resp[0].equals("AcceptedVoiceFrom")){
            SendJobToHandler("AcceptedVoiceFrom",str,"outgoingCallActivity");
        }
        else{
            SendJobToHandler("toastMsg",str,"MainActivity");
        }
    }
    public void CreateAllFriendsList(String csvlist,boolean addtoshare){
        String[] frList = csvlist.split(",");
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        //MyContacts.clear();
        for (String user:frList) {
            String[] tokens=user.split(";");
            String firstName=tokens[1];
            String lastName=tokens[2];
            String userId=tokens[0];
            boolean found=false;
            for (int j = 0; j < MyContacts.size(); j++) {
                if (MyContacts.get(j).getUsername().equals(userId)) {
                    found=true;
                    break;
                }
            }
            if(!found)//only adding new friends
                MyContacts.add(new MyContact(firstName,lastName,userId, false, false,false,""));
        }
        if(addtoshare) {
            SharedPreferences.Editor editor = getSharedPreferences("PeerInfo", MODE_PRIVATE).edit();
            editor.putString("allfriends", csvlist);
            editor.commit();
        }
    }
    public void CreateOnlineFriendsStatus(String csvonlinelist){
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        //reset online status to false
        for (int j = 0; j < MyContacts.size(); j++) {
            MyContacts.get(j).setStatus(false);
        }
        String[] frList = csvonlinelist.split(",");
        //update online status based on the frlist
        for (int i = 0; i < frList.length; i++) {
            for (int j = 0; j < MyContacts.size(); j++) {
                if (MyContacts.get(j).getUsername().equals(frList[i])) {
                    MyContacts.get(j).setStatus(true);
                }
            }
        }
        SharedPreferences.Editor editor = getSharedPreferences("PeerInfo", MODE_PRIVATE).edit();
        editor.putString("onlinefriends",csvonlinelist);
        editor.commit();
    }
    public void SendJobToHandler(String job,String str,String whichActivity) {
        Handler handler=null;
        if(whichActivity.equals("incommingCallActivity")){
            handler= ((MyGlobals) getApplicationContext()).getHandlerIncommingCallActivity();
        }
        else if(whichActivity.equals("MainActivity")) {
            handler= ((MyGlobals) getApplicationContext()).getHandlerMain();
        }
        else if(whichActivity.equals("outgoingCallActivity")) {
            handler= ((MyGlobals) getApplicationContext()).getHandlerOutgoingCallActivity();
        }
        Message msg = handler.obtainMessage();
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(job, str);
        msg.what = 0;
        msg.setData(bundleForLoader);
        handler.sendMessage(msg);
    }
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Bind to the service
        //sayHello(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Bind to the service
        //sayHello(1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Bind to the service
        //sayHello(1);
    }
    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putString("RequestType", "Friendaddress");
        super.onSaveInstanceState(state);

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //  Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean FindThreadByName(String name){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for(Thread th:threadArray){
            if(th.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public  void RemoveSharedPrefs(String st){
        getSharedPreferences(st, MODE_PRIVATE).edit().clear().commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // checks all of the possible cases from the options menu
        switch (item.getItemId()) {
            // Select on/off
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void logout(){
        RemoveSharedPrefs("PeerInfo");
        RemoveSharedPrefs("SelfInfo");
        RemoveSharedPrefs("ChatHistory");
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        MyContacts.clear();
        try {
            writeToStdin("exit");//send request to client() in native code running on separete thread to terminate itself
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.start);
        lfragment= new loginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1, lfragment, FRAG2_TAG).commit();
    }
    public void startClientThread(){
        class MyRunnable implements Runnable {
            public void run() {
                client();
            }
        }
        Thread thread = new Thread(new MyRunnable(), "clientThread");
        thread.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // RemoveSharedPrefs("PeerInfo");
       // RemoveSharedPrefs("SelfInfo");
       // RemoveSharedPrefs("ChatHistory");
        String dir=getApplicationContext().getApplicationInfo().dataDir;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);//start
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //RemoveSharedPrefs();
        ((MyGlobals) getApplicationContext()).setHandlerMain(new ChatMainHandler(this.getMainLooper()));
        if(!FindThreadByName("clientThread")) {//make sure you dont create thread that connects to server twice!
            startClientThread();
            SharedPreferences prefs = getSharedPreferences("PeerInfo", MODE_PRIVATE);
            if (!prefs.contains("login")){//brand new connection from this user or user pressed logout before
                lfragment = new loginFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame1,lfragment,FRAG2_TAG).commit();
            }
            else{//user logged in already once we know usr name and pswd so we bypass login screen but still go through login proceess requred by the server
                //start the client() in a separate thread
                login=prefs.getString("login","");
                try {
                    Thread.sleep(100);//this is needed in order for the newly created thread abouve to have enough time to set up the stdin pipe to which we write with writeToStdin() function below. Otherwise write will block
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    writeToStdin(login);//send request to client() in native code
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{// once person loges in once afterwards no more login screen will be shown i.e app goes directly to the mainfragment which shows list of online friends and ofline friends. If logout is pressed then sharedPrefs will be erased
            SharedPreferences prefs = getSharedPreferences("PeerInfo", MODE_PRIVATE);
            String csvfriends=prefs.getString("allfriends","");
            CreateAllFriendsList(csvfriends,false);
            onlinefriends();//update online friends by requesting the list from the server
        }
        Intent intent = getIntent();
        String message = intent.getStringExtra("uid");
        if(message!=null){
            try {
                ((MyGlobals) getApplicationContext()).SetTalking(true);
                goToChatAfterAcceptingCall(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String hangup = intent.getStringExtra("hangup");
        if(hangup!=null){
            Toast.makeText(this, "Call was terminated...", Toast.LENGTH_SHORT).show();
        }
        String rejected = intent.getStringExtra("rejected");
        if(rejected!=null){
            Toast.makeText(this, "Call was rejected...", Toast.LENGTH_SHORT).show();
        }
        String notanswered = intent.getStringExtra("notanswered");
        if(notanswered!=null){
            Toast.makeText(this, "Call not answered...", Toast.LENGTH_SHORT).show();
        }
    }
    public int id=1;
    public void VoiceCallNotification(String fullname,String uid,String direction) {
        Intent notificationIntent = new Intent("android.intent.category.LAUNCHER");
        notificationIntent.putExtra("fullname",fullname);
        notificationIntent.putExtra("uid",uid);
        if(direction.equals("incomming")) {
            notificationIntent.setClassName("com.google.sample.echo", "com.google.sample.echo.incommingCallActivity");
        }
        else if(direction.equals("outgoing")){
            notificationIntent.setClassName("com.google.sample.echo", "com.google.sample.echo.outgoingCallActivity");
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(notificationIntent);
    }
    public void addNotification(String msg) {
        android.support.v4.app.NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_green)
                        .setContentTitle("Chat")
                        .setContentText(msg+"...");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setLights(Color.YELLOW, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    //this is called when we just want to chat
    public void goToChat(View view) throws InterruptedException {
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        int selectCont = -1;
        ArrayList<String> peers = new ArrayList<String>();
        String usersBunch="";
        namesToShow="   ";
        chatusers="";
        for (int j = 0; j < MyContacts.size(); j++) {
            if (MyContacts.get(j).getSelected() && MyContacts.get(j).getStatus()) {
                selectCont = j;
                String uname=MyContacts.get(j).getUsername();
                peers.add(uname);
                usersBunch+=uname;
                String fullName=MyContacts.get(j).getfirstName()+" "+MyContacts.get(j).getlastName();
                if(chatusers==null){
                    chatusers=uname;
                }
                else{
                    chatusers+="_";//this is a token that will be used in case of a multiuser chat, by splitting with this token we can retreive the userids
                    chatusers+=uname;
                }
                if(namesToShow=="   "){
                    namesToShow+=fullName;
                }
                else {
                    namesToShow += "," + fullName;
                }
            }
        }
        ((MyGlobals) getApplicationContext()).SetChatUsers(chatusers);
        if (selectCont >= 0) {
            SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
            usersBunch+=prefs.getString("uname","");//create unique key for retreival of chat history
            ((MyGlobals) getApplicationContext()).SetUserIdOfChatPeer(usersBunch);
            InitiatePeerConnections(peers);
            inChat=true;
            for (MyContact contact: MyContacts) {
                if(contact.getUsername().equals(usersBunch)){//
                    contact.setBold(false);
                    contact.setMessage("");
                }
            }
          //creating the chat screen
            setContentView(R.layout.startchat);//start
            TextView nameToShow=(TextView)findViewById(R.id.chatname);
            nameToShow.setText(namesToShow);
            ((MyGlobals) getApplicationContext()).SetUdpHolePunch(true);
            Fragment fragment = new chatFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame1, fragment, FRAG2_TAG).addToBackStack(null).commit();
        } else {
            Toast.makeText(MainActivity.this, "please select an active user!", Toast.LENGTH_SHORT).show();
        }
    }
    //this is called when we accept voice call and go to chat as well
    public void goToChatAfterAcceptingCall(String uid) throws InterruptedException {

        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        int selectCont = -1;
        ArrayList<String> peers = new ArrayList<String>();
        String usersBunch="";
        chatusers="";
        namesToShow="   ";
        for (int j = 0; j < MyContacts.size(); j++) {
            if (MyContacts.get(j).getUsername().equals(uid)) {
                selectCont = j;
                String uname=MyContacts.get(j).getUsername();
                peers.add(uname);
                usersBunch+=uname;
                String fullName=MyContacts.get(j).getfirstName()+" "+MyContacts.get(j).getlastName();
                if(chatusers==null){
                    chatusers=uname;
                }
                else{
                    chatusers+="_";//this is a token that will be used in case of a multiuser chat, by splitting with this token we can retreive the userids
                    chatusers+=uname;
                }
                if(namesToShow=="   "){
                    namesToShow+=fullName;
                }
                else {
                    namesToShow += "," + fullName;
                }
            }
        }
        ((MyGlobals) getApplicationContext()).SetChatUsers(chatusers);
        if (selectCont >= 0) {
            SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
            usersBunch+=prefs.getString("uname","");//create unique key for retreival of chat history
            ((MyGlobals) getApplicationContext()).SetUserIdOfChatPeer(usersBunch);
            inChat=true;
            for (MyContact contact: MyContacts) {
                if(contact.getUsername().equals(usersBunch)){//
                    contact.setBold(false);
                    contact.setMessage("");
                }
            }
            //creating the chat screen
            setContentView(R.layout.startchat);//start
            TextView nameToShow=(TextView)findViewById(R.id.chatname);
            nameToShow.setText(" voice call with: "+namesToShow);
            ((MyGlobals) getApplicationContext()).SetUdpHolePunch(true);
            Fragment fragment = new chatFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame1, fragment, FRAG2_TAG).addToBackStack(null).commit();
        } else {
            Toast.makeText(MainActivity.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
        }
    }
    //this is called when we initiate voice connection
    public boolean goToVoiceChat(View view) throws InterruptedException {
        boolean talking=((MyGlobals) getApplicationContext()).GetTalking();
        if (!talking){
            goToChat(view);
            if(chatusers.isEmpty()) {
                //Toast.makeText(MainActivity.this, "please select an active user!", Toast.LENGTH_SHORT).show();
                return false;
            }
            String[] tokens = chatusers.split("_");
            VoiceCallNotification(namesToShow.trim(), tokens[1], "outgoing");
            SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
            String selfUserName = prefs.getString("selfUserName", "error");
            String FullCall = "IncomingVoiceFrom:" + selfUserName + ":IncomingVoiceFrom:" + chatusers;//IncomingVoiceFrom serves as a message
            try {
                writeToStdin(FullCall);//send request to client() in native code
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{//here we want to finish conversation
            Button CallOrHangupButton=(Button)findViewById(R.id.voiceCall);
            CallOrHangupButton.setBackgroundResource(R.drawable.acceptrectange);
            CallOrHangupButton.setText("Call");
            ((MyGlobals) getApplicationContext()).SetTalking(false);
            SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
            String selfUserName = prefs.getString("selfUserName", "error");
            String FullCall = "HangupVoiceFrom:" + selfUserName + ":HangupVoiceFrom:" + chatusers;//IncomingVoiceFrom serves as a message
            try {
                writeToStdin(FullCall);//send request to client() in native code
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public void AddMsgToHistory(String senderUid,String chatmsg){
        SharedPreferences prefs = getSharedPreferences("ChatHistory", MODE_PRIVATE);
        //String uid=((MyGlobals) getApplicationContext()).GetUserIdOfChatPeer();
        String hist="";
        if (prefs.contains(senderUid)) {
            hist = prefs.getString(senderUid, "");
            hist += token + chatmsg;//adding current msg to history
        }
        else {
            hist += chatmsg;//adding current msg to history
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(senderUid,hist);//save login info in sharedPrefs
        editor.commit();
    }
    public void send(View view) throws IOException {
        EditText chatText = (EditText) findViewById(R.id.txttosnd);
        SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
        String selfUserName=prefs.getString("selfUserName","error");//self username should always be available
        String chatmsg=chatText.getText().toString();
        String Fullchatmsg = "chat:"+selfUserName+":"+chatmsg;
        if (chatmsg.length() >=1) {
            String uid=((MyGlobals) getApplicationContext()).GetUserIdOfChatPeer();
            chatAdapter.add(chatmsg);
            chatAdapter.notifyDataSetChanged();
            AddMsgToHistory(uid,chatmsg);
            Fullchatmsg+=(":"+chatusers);// chatusers are peer user ids to whom the chat is dirrected
            writeToStdin(Fullchatmsg);
        }
        chatText.setText("");
    }
    public void AppendToChatScreen(String senderUserid,String chatmsg) {
        if (chatmsg.length() > 0 && !chatmsg.equals("\n")) {
            String shortmsg="";
            if(chatmsg.length()>7)
                shortmsg=chatmsg.substring(0,7);
            else
                shortmsg=chatmsg;
            String uid=((MyGlobals) getApplicationContext()).GetUserIdOfChatPeer(); //this is set when chat button is pressed
            chatmsg = "abdulmanatkhabib:" + chatmsg;
            AddMsgToHistory(senderUserid,chatmsg);
            if(senderUserid.equals(uid) && inChat) {//adding to the screen only if the chat screeen for the peer that the message is intended for is open
                chatAdapter.add(chatmsg);
                chatAdapter.notifyDataSetChanged();
            }
            else{//make the sender name in your peers list bold and show part of the message as well, like in skype
                ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
                String fname="";
                String lname="";
                for (MyContact contact: MyContacts) {
                    if(contact.getUsername().equals(senderUserid)){
                        contact.setBold(true);
                        contact.setMessage(shortmsg);//what we set just above
                        fname=contact.getfirstName();
                        lname=contact.getlastName();
                    }
                }
                SendJobToHandler("mainFragment","","MainActivity");
                addNotification(fname+" "+lname+" says:"+shortmsg);
            }
        }
    }
    public void goToRegister(View view) {
        if(!FindThreadByName("clientThread")){
            startClientThread();
        }
       /* else{
            try {
                writeToStdin("exit");//send request to client() in native code running on separete thread to terminate itself
            } catch (IOException e) {
                e.printStackTrace();
            }
            startClientThread();// need to restart client thread when registering new user otherwise cant send udp to server
        }*/
        rfragment = new registrationFragment();
        getSupportFragmentManager().beginTransaction().remove(lfragment).add(R.id.frame1, rfragment, FRAG2_TAG).commit();
    }
    public void backtologin(View view) {
        getSupportFragmentManager().beginTransaction().remove(rfragment).add(R.id.frame1, lfragment, FRAG2_TAG).commit();
    }
    public void login(View view) throws InterruptedException {
        if(!FindThreadByName("clientThread")){
            startClientThread();
        }
        Thread.sleep(100);//this is needed in order for the newly created thread abouve to have enough time to set up the stdin pipe to which we write with writeToStdin() function below. Otherwise write will block
        EditText UserName = (EditText) findViewById(R.id.usernameEntry);
        String unameSt = UserName.getText().toString();
        EditText Password = (EditText) findViewById(R.id.passwordEntry);
        String pswdSt = Password.getText().toString();
        login = "Login::," + unameSt + "," + pswdSt + "\0";
        //saving self username
        SharedPreferences.Editor editor = getSharedPreferences("SelfInfo", MODE_PRIVATE).edit();
        editor.putString("selfUserName",unameSt);//save login info in sharedPrefs
        editor.commit();
        try {
            writeToStdin(login);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void register(View view) {

        String FNAME = "fname";
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("RequestType", "Register");
        EditText fname = (EditText) findViewById(R.id.fnameEditText);
        String fnameSt = fname.getText().toString();
        bundleForLoader.putString(FNAME, fnameSt);

        EditText lname = (EditText) findViewById(R.id.lnameEditText);
        String lnameSt = lname.getText().toString();
        bundleForLoader.putString("lname", lnameSt);

        EditText uname = (EditText) findViewById(R.id.unameEditText);
        String unameSt = uname.getText().toString();
        bundleForLoader.putString("uname", unameSt);

        EditText pswd = (EditText) findViewById(R.id.rpasswordEdit);
        String pswdSt = pswd.getText().toString();
        bundleForLoader.putString("pwd", pswdSt);

        EditText email = (EditText) findViewById(R.id.remailEditText);
        String emailSt = uname.getText().toString();
        bundleForLoader.putString("email", emailSt);

       String request = "Register::," + unameSt + "," + pswdSt + "," + fnameSt + "," + lnameSt + "," + emailSt + "\0";
        try {
            writeToStdin(request);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
        login = "Login::," + unameSt + "," + pswdSt + "\0";
        //saving self username
        SharedPreferences.Editor editor = getSharedPreferences("SelfInfo", MODE_PRIVATE).edit();
        editor.putString("selfUserName",unameSt);//save login info in sharedPrefs
        editor.commit();
    }
    public void allfriends() {
        String request = "allfriends" + "\0";
        try {
            writeToStdin(request);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onlinefriends() {
        String request = "onlinefriends" + "\0";
        try {
            writeToStdin(request);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void InitiatePeerConnections(ArrayList<String> peers){
        String request = "friendaddress:";// + unameSt + "\0";
        int cnt=0;
        for(String peer:peers){
            if(cnt!=0) {
                request += "," + peer;
            }
            else{
                request += peer;
            }
            cnt++;
        }
        request+="\0";
        try {
            writeToStdin(request);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public final static String FRAG2_TAG = "FRAG2";

    static public void writeToStdin(String msg) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/data/data/com.google.sample.echo/infile");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ;
        try {
            out.write(msg.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PopulateContactsList() {
        contactsadapter = new contactArrayAdapter(this, 0, ((MyGlobals) getApplicationContext()).GetContacts());
        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(contactsadapter);
    }
    public void PopulateChatList() {
       // RemoveSharedPrefs("ChatHistory");
        chatAdapter = new chatArrayAdapter(this, 0);
        String uid=((MyGlobals) getApplicationContext()).GetUserIdOfChatPeer();
        SharedPreferences prefs = getSharedPreferences("ChatHistory", MODE_PRIVATE);
        if (prefs.contains(uid)){
            String hist=prefs.getString(uid,"");//history of chat with that uid
            String[] hists = hist.split(token);
            for(String msg:hists) {
                chatAdapter.add(msg);
            }
        }
        chatList = (ListView) findViewById(R.id.chat1);
        chatList.setAdapter(chatAdapter);
    }
    public void PopulateRequestsList() {
        requestAdapter= new requestsArrayAdapter(this, 0);
        for(String peer:((MyGlobals) getApplicationContext()).GetPeerRequests()){
            requestAdapter.add(peer);
        }
        TextView requests=(TextView)findViewById(R.id.requests);
        if(requestAdapter.isEmpty()) {
            requests.setText("");
        }
        else {
            requests.setText("Requests to be approved:");
        }
        requestList = (ListView) findViewById(R.id.requestlist);
        requestList.setAdapter(requestAdapter);
    }

}
