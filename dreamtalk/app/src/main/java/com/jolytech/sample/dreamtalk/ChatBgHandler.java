package com.jolytech.sample.dreamtalk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by aj on 3/31/17.
 */

class ChatBgHandler extends Handler {
    private static final String TAG = "from handler:";

    public ChatBgHandler(Looper looper) {
        super(looper);
    }
    @Override
    public void handleMessage(Message msg) {
        Bundle bundleForLoader =msg.getData();
        String RequestType = bundleForLoader.getString("RequestType");
        Log.d("abdulhandler", "handleMessage in bg thread " + RequestType + " in " + Thread.currentThread() + "thred_id " + Thread.currentThread().getId());

        //sending response to main thread
        Bundle BundleForMain = new Bundle();
        BundleForMain.putString("RequestType", "resultformain");

    }

}
