package com.google.sample.echo;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import com.google.sample.echo.data.MyContact;
import android.support.v4.app.FragmentManager;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity  {


    ArrayAdapter<String> chatAdapter;
    ListView chatList;
    ListView requestList;
    final static String token="zilimbekdipsheetmagamedovhasanjemalusho";////this is the token to separate the chat hist. It has to be unique so that user text is not mistaken for it, that is why comma could not be used!
    String login;
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
                String RequestType = bundleForLoader.getString("chat");
                if (RequestType.equals("udp hole punch\n")) {
                    ((MyGlobals) getApplicationContext()).SetUdpHolePunch(true);
                    Fragment fragment =new chatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame1, fragment, FRAG2_TAG).addToBackStack(null).commit();
                } else {
                    AppendToChatScreen(RequestType);
                }
            }
            else if(bundleForLoader.containsKey("mainFragment")){
                ((MyGlobals) getApplicationContext()).GetPeerRequests().add("givi");
                ((MyGlobals) getApplicationContext()).GetPeerRequests().add("vova");
                setContentView(R.layout.tmp);//start
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
                tabLayout.addTab(tabLayout.newTab().setText("Add Contacts"));
                viewPager = (ViewPager) findViewById(R.id.pager);
                adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),getApplicationContext());
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                viewPager.setAdapter(adapter);
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
                View v=adapter.getTabView(numPeerRequest,getApplicationContext());
                tabLayout.getTabAt(1).setCustomView(v);
                ((MyGlobals) getApplicationContext()).SetTab(tabLayout.getTabAt(1));
            }
        }
    }
    ViewPager viewPager;
    PageAdapter adapter;
    TabLayout tabLayout;
    Toolbar toolbar;
    public void submit(View view){
       // SendJobToMainThread("refresh","");
    }
// this is called from the native client() function when chat text need to be passed to the main thread
    public void ProcessRequest(String str) {
        String[] resp = str.split(":");
        if (resp.length == 4 && resp[3].equals("ok")) {
            SharedPreferences.Editor editor = getSharedPreferences("PeerInfo", MODE_PRIVATE).edit();
            editor.putString("login",login);//save login info in sharedPrefs
            editor.commit();
            allfriends();
        } else if (resp.length == 4 && resp[2].equals("allfriends")) {
            String frlistSt = resp[3].substring(0, resp[3].length());
            CreateAllFriendsList(frlistSt,true);
            onlinefriends();
        } else if (resp.length > 2 && resp[2].equals("onlinefriends")) {
            if (resp.length == 4) {
                String frlistSt = resp[3].substring(0, resp[3].length());
                CreateOnlineFriendsStatus(frlistSt);
            }
            SendJobToMainThread("mainFragment",str);
        }
        else if((resp.length ==2 && resp[0].equals("chat"))){
            SendJobToMainThread("chat",resp[1]);
        }
        else{
            SendJobToMainThread("toastMsg",str);
        }
    }
    public void CreateAllFriendsList(String csvlist,boolean addtoshare){
        String[] frList = csvlist.split(",");
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        MyContacts.clear();
        for (int i = 0; i < frList.length; i++) {
                MyContacts.add(new MyContact("", "", frList[i], false, false));
        }
        if(addtoshare) {
            SharedPreferences.Editor editor = getSharedPreferences("PeerInfo", MODE_PRIVATE).edit();
            editor.putString("allfriends", csvlist);
            editor.commit();
        }
    }
    public void CreateOnlineFriendsStatus(String csvonlinelist){
        String[] frList = csvonlinelist.split(",");
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
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
    public void SendJobToMainThread(String job,String str) {
        Handler mainhd = ((MyGlobals) getApplicationContext()).getHandlerMain();
        Message msg = mainhd.obtainMessage();
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(job, str);
        msg.what = 0;
        msg.setData(bundleForLoader);
        mainhd.sendMessage(msg);
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

        try {
            writeToStdin("exit");//send request to client() in native code running on separete thread to terminate itself
        } catch (IOException e) {
            e.printStackTrace();
        }
        Fragment fragment = new loginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1,fragment,FRAG2_TAG).commit();
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);//start
        //RemoveSharedPrefs();
        ((MyGlobals) getApplicationContext()).setHandlerMain(new ChatMainHandler(this.getMainLooper()));
        if(!FindThreadByName("clientThread")) {//make sure you dont create thread that connects to server twice!
            //start the client() in a separate thread
            startClientThread();
            SharedPreferences prefs = getSharedPreferences("PeerInfo", MODE_PRIVATE);
            if (!prefs.contains("login")){//brand new connection from this user or user pressed logout before
                loginFragment fragment = new loginFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame1,fragment,FRAG2_TAG).commit();
            }
            else{//user logged in already once we know usr name and pswd so we bypass login screen but still go through login proceess requred by the server
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
    }
    public void goToChat(View view) throws InterruptedException {
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        int selectCont = -1;
        ArrayList<String> peers = new ArrayList<String>();
        String usersBunch="";
        for (int j = 0; j < MyContacts.size(); j++) {
            if (MyContacts.get(j).getSelected() && MyContacts.get(j).getStatus()) {
                selectCont = j;
                String uname=MyContacts.get(j).getUsername();
                peers.add(uname);
                usersBunch+=uname;
            }
        }
        if (selectCont >= 0) {
            SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
            usersBunch+=prefs.getString("uname","");//create unique key for retreival of chat history
            ((MyGlobals) getApplicationContext()).SetUserIdOfChatPeer(usersBunch);
            InitiatePeerConnections(peers);
        } else {
            Toast.makeText(MainActivity.this, "please select an active user!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddMsgToHistory(String chatmsg){
        SharedPreferences prefs = getSharedPreferences("ChatHistory", MODE_PRIVATE);
        String uid=((MyGlobals) getApplicationContext()).GetUserIdOfChatPeer();
        String hist="";
        if (prefs.contains(uid)) {
            hist = prefs.getString(uid, "");
            hist += token + chatmsg;//adding current msg to history
        }
        else {
            hist += chatmsg;//adding current msg to history
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(uid,hist);//save login info in sharedPrefs
        editor.commit();
    }
    public void send(View view) throws IOException {
        EditText chatText = (EditText) findViewById(R.id.txttosnd);
        String chatmsg = chatText.getText().toString() + "\n";
        if (chatmsg.length() > 1) {
            chatAdapter.add(chatmsg);
            chatAdapter.notifyDataSetChanged();
            writeToStdin(chatmsg);
            AddMsgToHistory(chatmsg);
        }
        chatText.setText("");
    }

    public void AppendToChatScreen(String chatmsg) {

        if (chatmsg.length() > 0 && !chatmsg.equals("\n")) {
            chatmsg = "abdulmanatkhabib:" + chatmsg;
            chatAdapter.add(chatmsg);
            chatAdapter.notifyDataSetChanged();
            AddMsgToHistory(chatmsg);
        }
    }

    //int LOGIN_LOADER_ID = 0;

    public void goToRegister(View view) {
        registrationFragment fragment = new registrationFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame1, fragment, FRAG2_TAG).addToBackStack(null).commit();
    }

    public void login(View view) throws InterruptedException {
        startClientThread();
        Thread.sleep(100);//this is needed in order for the newly created thread abouve to have enough time to set up the stdin pipe to which we write with writeToStdin() function below. Otherwise write will block
        EditText UserName = (EditText) findViewById(R.id.usernameEntry);
        String unameSt = UserName.getText().toString();
        EditText Password = (EditText) findViewById(R.id.passwordEntry);
        String pswdSt = Password.getText().toString();
        login = "Login::," + unameSt + "," + pswdSt + "\0";
        //saving self username
        SharedPreferences.Editor editor = getSharedPreferences("SelfInfo", MODE_PRIVATE).edit();
        editor.putString("uname",unameSt);//save login info in sharedPrefs
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

       String request = "Register::," + unameSt + "," + pswdSt + "," + fname + "," + lname + "," + email + "\0";
        try {
            writeToStdin(request);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
        //LoaderManager.LoaderCallbacks<String> callback = MainActivity.this;
        //getSupportLoaderManager().restartLoader(LOGIN_LOADER_ID, bundleForLoader, callback);

    }

    public void allfriends() {
        String request = "allfriends" + "\0";
        try {
            writeToStdin(request);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Bundle bundleForLoader = new Bundle();
        // bundleForLoader.putString("RequestType", "AllFriends");
               //LoaderManager.LoaderCallbacks<String> callback = MainActivity.this;
        //getSupportLoaderManager().restartLoader(LOGIN_LOADER_ID, bundleForLoader, callback);
    }

    public void onlinefriends() {
        String request = "onlinefriends" + "\0";
        try {
            writeToStdin(request);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Bundle bundleForLoader = new Bundle();
        //bundleForLoader.putString("RequestType", "OnlineFriends");
        //LoaderManager.LoaderCallbacks<String> callback = MainActivity.this;
        //getSupportLoaderManager().restartLoader(LOGIN_LOADER_ID, bundleForLoader, callback);
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

    public void writeToStdin(String msg) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/data/data/com.google.sample.echo/files/infile");
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

    public String readFromStdout() {
        FileReader in = null;
        try {
            in = new FileReader("/data/data/com.google.sample.echo/files/outfile");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                if (in.ready()) {
                    char[] cbuff = new char[200];
                    int numread = in.read(cbuff);
                    String response = String.valueOf(cbuff);
                    String[] res = response.split("AbdulmanatKhabib:");// this is done cos there is a bug that sometimes returns garbage at the begining of the sent string
                    if (res.length == 3) {
                        return res[1];
                    } else {
                        continue;
                    }
                }
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public void PopulateContactsList() {
        ArrayAdapter<MyContact> adapter = new contactArrayAdapter(this, 0, ((MyGlobals) getApplicationContext()).GetContacts());
        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(adapter);
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
        requestsArrayAdapter requestAdapter= new requestsArrayAdapter(this, 0);
        for(String peer:((MyGlobals) getApplicationContext()).GetPeerRequests()){
            requestAdapter.add(peer);
        }
        /*SharedPreferences prefs = getSharedPreferences("PeerInfo", MODE_PRIVATE);
        if (prefs.contains("requests")){
            String request=prefs.getString("requests","");//requests from the peers
            String[] requests = request.split(",");
            for(String req:requests) {
                requestAdapter.add(req);
            }
        }
        requestAdapter.add("tmpvova");
        requestAdapter.add("tmpilo");
        requestAdapter.add("tmpasdvfb");        */

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
