package com.google.sample.echo;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

/**
 * Created by aj on 3/18/17.
 */
public class ServerConnectionService extends Service {
    /** Command to the service to display a message */
    static final int MSG_SAY_registration = 1;
    static final int MSG_SAY_login = 2;

    /**
     * Handler of incoming messages from clients.
     */

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_registration:
                    Toast.makeText(getApplicationContext(), "registration!"+((MyGlobals)getApplicationContext()).getCnt(), Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SAY_login:
                    Toast.makeText(getApplicationContext(), "login!"+((MyGlobals)getApplicationContext()).getCnt(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**HandlerThread
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        MyGlobals mApp = ((MyGlobals)getApplicationContext());
        int cnt = mApp.getCnt();
        mApp.setCnt(cnt++);
        return mMessenger.getBinder();
    }
}

