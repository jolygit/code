package com.jolytech.sample.dreamtalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class outgoingCallActivity extends AppCompatActivity {
    boolean timeOver=true;
    Handler callHandler;
    String uid;
    private MediaPlayer mediaPlayer = null;
    class CallHandler extends Handler {
        private static final String TAG = "from handler:";
        public CallHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Bundle bundleForLoader = msg.getData();
            if(timeOver && bundleForLoader.containsKey("call_not_answered")) {
                Intent mainIntent = new Intent("android.intent.category.LAUNCHER");
                mainIntent.setClassName("com.jolytech.sample.dreamtalk", "com.jolytech.sample.dreamtalk.MainActivity");
                mainIntent.putExtra("notanswered","");
                startActivity(mainIntent);
                mediaPlayer.stop();
                finish();
            }
            else if(bundleForLoader.containsKey("RejectedVoiceFrom")) {
                timeOver=false;
                Intent mainIntent = new Intent("android.intent.category.LAUNCHER");
                mainIntent.setClassName("com.jolytech.sample.dreamtalk", "com.jolytech.sample.dreamtalk.MainActivity");
                mainIntent.putExtra("rejected","");
                startActivity(mainIntent);
                mediaPlayer.stop();
                finish();
            }
            else if(bundleForLoader.containsKey("AcceptedVoiceFrom")) {
                timeOver=false;
                Intent mainIntent = new Intent("android.intent.category.LAUNCHER");
                mainIntent.setClassName("com.jolytech.sample.dreamtalk", "com.jolytech.sample.dreamtalk.MainActivity");
                mainIntent.putExtra("uid",uid);//accepted from this uid i.e we need to go to chat for this uid
                startActivity(mainIntent);
                mediaPlayer.stop();
                finish();
            }
        }
    }
    public void SendJobToMainThread(String job,String str) {
        Message msg = callHandler.obtainMessage();
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(job, str);
        msg.what = 0;
        msg.setData(bundleForLoader);
        callHandler.sendMessageDelayed(msg,16000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        callHandler = new CallHandler(this.getMainLooper());
        ((MyGlobals) getApplicationContext()).setHandlerOutgoingCallActivity(callHandler);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_call);
        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        uid = intent.getStringExtra("uid");
        TextView toWhomcallingPersonFullName = (TextView) findViewById(R.id.to_whom_calling_name);
        toWhomcallingPersonFullName.setText(fullname);
        SendJobToMainThread("call_not_answered", "");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), alarmSound);//R.raw.bachring
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    public void cancel(View v){
        timeOver=false;
        mediaPlayer.stop();
        SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
        String selfUserName=prefs.getString("selfUserName","error");
        String chatusers= ((MyGlobals) getApplicationContext()).GetChatUsers();
        String FullCall = "CanceledVoiceFrom:"+selfUserName+":CanceledVoiceFrom:"+chatusers;//IncomingVoiceFrom serves as a message
        try {
            writeToStdin(FullCall);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent mainIntent = new Intent("android.intent.category.LAUNCHER");
        mainIntent.setClassName("com.jolytech.sample.dreamtalk", "com.jolytech.sample.dreamtalk.MainActivity");
        startActivity(mainIntent);
        finish();
    }
    static public void writeToStdin(String msg) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/data/data/com.jolytech.sample.dreamtalk/infile");
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
}
