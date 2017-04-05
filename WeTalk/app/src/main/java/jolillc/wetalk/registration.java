package jolillc.wetalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class registration extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int FORECAST_LOADER_ID = 0;
    public void backtologin(View view) {
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            List<String> numbers = new ArrayList<String>();

            public String client(String registration) throws IOException {
             Socket echoSocket = new Socket("192.168.1.117",9877);
             PrintWriter out =
                                new PrintWriter(echoSocket.getOutputStream(), true);
             InputStream in=echoSocket.getInputStream();
             out.println(registration);
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
                String unameSt = args.getString("uname");
                String pswdSt = args.getString("pwd");
                String fname = args.getString("fname");
                String lname = args.getString("lname");
                String email = args.getString("email");
                String registration="Register::,"+unameSt+","+pswdSt+","+fname+","+lname+","+email+"\0";
                String serverResponse=null;
                try {
                    serverResponse=client(registration);//getString(R.string.registrationFeiled);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String subst=serverResponse.substring(0,serverResponse.length()-1);
                String[] resp=subst.split(":");
                if(resp.length==4 && resp[3].equals("ok")) {
                return "ok";
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(registration.this,data,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
