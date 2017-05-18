package com.jolytech.sample.dreamtalk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class incommingCallActivity extends AppCompatActivity {
    Handler callHandler;
    String uid;//calling person user id
    boolean timeOver=true;
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
                startActivity(mainIntent);
                mediaPlayer.stop();
                finish();
                String fullname=FullName();
                addNotification(fullname);
            }
            else if(bundleForLoader.containsKey("CanceledVoiceFrom")) {
                timeOver=false;
                Intent mainIntent = new Intent("android.intent.category.LAUNCHER");
                mainIntent.setClassName("com.jolytech.sample.dreamtalk", "com.jolytech.sample.dreamtalk.MainActivity");
                startActivity(mainIntent);
                mediaPlayer.stop();
                finish();
            }
        }
    }
    public String FullName(){
        String namesToShow="";
        ArrayList<MyContact> MyContacts = ((MyGlobals) getApplicationContext()).GetContacts();
        for (int j = 0; j < MyContacts.size(); j++) {
            if (MyContacts.get(j).getUsername().equals(uid)) {
                namesToShow=MyContacts.get(j).getfirstName()+" "+MyContacts.get(j).getlastName();
                break;
            }
        }
        return namesToShow;
    }
    public void SendJobToMainThread(String job,String str) {
        Message msg = callHandler.obtainMessage();
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(job, str);
        msg.what = 0;
        msg.setData(bundleForLoader);
        callHandler.sendMessageDelayed(msg,15000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        callHandler = new CallHandler(this.getMainLooper());
        ((MyGlobals) getApplicationContext()).setHandlerIncommingCallActivity(callHandler);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomming_call);
        getWindow().addFlags(//WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        uid = intent.getStringExtra("uid");
        TextView callingPersonFullName = (TextView) findViewById(R.id.calling_person);
        callingPersonFullName.setText(fullname);
        SendJobToMainThread("call_not_answered", "");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), alarmSound);//R.raw.bachring
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    public void accept(View v){
        timeOver=false;
        mediaPlayer.stop();
        SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
        String selfUserName=prefs.getString("selfUserName","error");
        String chatusers= "_"+uid;//we can not retveive it from MyGlobals because we have not selected anyone to call, instead we are being called by online contact
        String FullCall = "AcceptedVoiceFrom:"+selfUserName+":AcceptedVoiceFrom:"+chatusers;//IncomingVoiceFrom serves as a message
        try {
            writeToStdin(FullCall);//send request to client() in native code
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent mainIntent = new Intent("android.intent.category.LAUNCHER");
        mainIntent.setClassName("com.jolytech.sample.dreamtalk", "com.jolytech.sample.dreamtalk.MainActivity");
        mainIntent.putExtra("uid",uid);//this way we go to fragment of the chat with uid see in MainActivity onCreate
        startActivity(mainIntent);
        finish();
    }
    public void reject(View v){
        timeOver=false;
        mediaPlayer.stop();
        SharedPreferences prefs = getSharedPreferences("SelfInfo", MODE_PRIVATE);
        String selfUserName=prefs.getString("selfUserName","error");
        String chatusers= "_"+uid;//we can not retveive it from MyGlobals because we have not selected anyone to call, instead we are being called by online contact
        String FullCall = "RejectedVoiceFrom:"+selfUserName+":RejectedVoiceFrom:"+chatusers;//IncomingVoiceFrom serves as a message
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
    public void addNotification(String name) {
        android.support.v4.app.NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_green)
                        .setContentTitle(name)
                        .setContentText("Missed call");
        Intent notificationIntent = new Intent("android.intent.category.LAUNCHER");
        notificationIntent.setClassName("com.jolytech.sample.dreamtalk", "com.jolytech.sample.dreamtalk.MainActivity");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setLights(Color.YELLOW, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
