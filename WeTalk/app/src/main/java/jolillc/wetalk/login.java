package jolillc.wetalk;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.LoginFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import jolillc.wetalk.ServerConnectionService;
import jolillc.wetalk.data.MyContact;

public class login extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int FORECAST_LOADER_ID = 1;
    Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };
    public void sayHello(int which) {
        if (!mBound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, which, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
        //bindService(new Intent(this, ServerConnectionService.class), mConnection,Context.BIND_AUTO_CREATE);

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Bind to the service
        //sayHello(1);
    }

    public void login(View view) {
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("RequestType", "Login");
        EditText UserName = (EditText) findViewById(R.id.usernameEntry);
        String unameSt = UserName.getText().toString();
        bundleForLoader.putString("uname", unameSt);
        EditText Password = (EditText) findViewById(R.id.passwordEntry);
        String pswdSt = Password.getText().toString();
        bundleForLoader.putString("pwd", pswdSt);

        LoaderManager.LoaderCallbacks<String> callback = login.this;
        getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, bundleForLoader, callback);
    }
    public void goToRegister(View view) {
        registrationFragment fragment = new registrationFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame1, fragment, FRAG2_TAG).addToBackStack(null).commit();
    }
    public void register(View view) {
        String FNAME="fname";
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

        LoaderManager.LoaderCallbacks<String> callback = login.this;
        getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, bundleForLoader, callback);

    }
    public void allfriends(){
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("RequestType", "AllFriends");
        LoaderManager.LoaderCallbacks<String> callback = login.this;
        getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, bundleForLoader, callback);
    }
    public void onlinefriends(){
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("RequestType", "OnlineFriends");
        LoaderManager.LoaderCallbacks<String> callback = login.this;
        getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, bundleForLoader, callback);
    }
    public final static String FRAG2_TAG = "FRAG2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        loginFragment fragment = new loginFragment();
        fragmentTransaction.add(R.id.frame1,fragment,FRAG2_TAG);
        fragmentTransaction.commit();


        /*Intent intent = getIntent();
        String message = intent.getStringExtra("userid");
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_login);
        layout.addView(textView);*/
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            List<String> numbers = new ArrayList<String>();

            public String client(String registration) throws IOException {
                MyGlobals mApp = ((MyGlobals)getApplicationContext());
                Socket echoSocket = ((MyGlobals)getApplicationContext()).GetSocket();
                if(echoSocket==null){
                    ((MyGlobals)getApplicationContext()).CreateSocket();
                    echoSocket=((MyGlobals)getApplicationContext()).GetSocket();
                }

                OutputStream out =echoSocket.getOutputStream();
                InputStream in=echoSocket.getInputStream();
                out.flush();
                out.write(registration.getBytes());
                String response=null;
                while(true){
                    if(in.available()>0){
                        int num=in.available();
                        byte[] cbuff= new byte[num];
                        in.read(cbuff);
                        String str1 = new String(cbuff);
                        response=str1;//System.out.println("echo: " + String.valueOf(num)+str1);//+ cbuff.toString(
                        break;
                    }
                }
                return response;
            }

            @Override
            public String loadInBackground() {
                String request = null;// needed "\0" for server to work cos  read system call will read till it reaches "\0'
                String serverResponse = null;
                String RequestType = args.getString("RequestType");
                if(RequestType.equals("Login")) {
                    String unameSt = args.getString("uname");
                    String pswdSt = args.getString("pwd");
                    request = "Login::," + unameSt + "," + pswdSt + "\0";// needed "\0" for server to work cos  read system call will read till it reaches "\0'
                    serverResponse = null;
                }
                else if(RequestType.equals("AllFriends")){
                    request ="request:allfriends"+"\0";
                }
                else if(RequestType.equals("OnlineFriends")){
                    request ="request:onlinefriends"+"\0";
                }
                else if(RequestType.equals("Register")){
                    String unameSt = args.getString("uname");
                    String pswdSt = args.getString("pwd");
                    String fname = args.getString("fname");
                    String lname = args.getString("lname");
                    String email = args.getString("email");
                    request="Register::,"+unameSt+","+pswdSt+","+fname+","+lname+","+email+"\0";
                }
                try {
                    serverResponse=client(request);//getString(R.string.registrationFeiled);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String subst=serverResponse.substring(0,serverResponse.length()-1);
                String[] resp=subst.split(":");
                if(resp.length==4 && resp[3].equals("ok")) {
                    return "ok";
                }
                else if(resp.length==4 && resp[2].equals("allfriends")) {
                    String frlistSt=resp[3].substring(0,resp[3].length()-1);
                    String[] frList=frlistSt.split(",");
                    ArrayList<MyContact> MyContacts = ((MyGlobals)getApplicationContext()).GetContacts();
                    for(int i=0;i<frList.length;i++) {
                        MyContacts.add(new MyContact(frList[i], frList[i], false, false));
                    }
                    return "allfriends";
                }
                else if(resp[2].equals("onlinefriends")) {
                    if(resp.length==4) {
                        String frlistSt = resp[3].substring(0, resp[3].length());
                        String[] frList = frlistSt.split(",");
                        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
                        for (int i = 0; i < frList.length; i++) {
                            for (int j = 0; j < MyContacts.size(); j++) {
                                if (MyContacts.get(j).getfirstName().equals(frList[i])) {
                                    MyContacts.get(j).setStatus(true);
                                }
                            }
                        }
                    }
                    return "onlinefriends";
                }
                else {
                    return serverResponse;
                }
            }
            @Override
            protected void onStartLoading() {
                forceLoad();
            }
            public void deliverResult(String data) {
                //result = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data.equals("ok")) {
            mainFragment fragment = new mainFragment();
            getFragmentManager().beginTransaction().replace(R.id.frame1,fragment,FRAG2_TAG).addToBackStack(null).commit();
            allfriends();
        }
        else if(data.equals("allfriends")){
            onlinefriends();
        }
        else if(data.equals("onlinefriends")){
            //create our new array adapter
            ArrayAdapter<MyContact> adapter = new contactArrayAdapter(this, 0, ((MyGlobals)getApplicationContext()).GetContacts());
            //Find list view and bind it with the custom adapter
            ListView listView = (ListView) findViewById(R.id.customListView);
            listView.setAdapter(adapter);
            Toast.makeText(login.this,data,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
