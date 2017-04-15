package com.google.sample.echo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by aj on 3/22/17.
 */

public class chatFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chat1, container, false);
    }
    @Override
    public void onResume(){
        ((MainActivity)getActivity()).PopulateChatList();
        super.onResume();
    }
}
