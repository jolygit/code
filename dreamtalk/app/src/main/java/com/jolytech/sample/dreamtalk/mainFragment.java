package com.jolytech.sample.dreamtalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class mainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_main, container, false);
        Button CallOrHangupButton=(Button)v.findViewById(R.id.voiceCall);
        boolean talking=((MyGlobals) getActivity().getApplicationContext()).GetTalking();//this is boolean that is true when voice conversation is on, false otherwise
        if(talking) {
            CallOrHangupButton.setBackgroundResource(R.drawable.rejectrectangle);
            CallOrHangupButton.setText("Hang Up");
        }
        else
        {
            CallOrHangupButton.setBackgroundResource(R.drawable.acceptrectange);
            CallOrHangupButton.setText("Call");
        }
        return v;
    }
    @Override
    public void onResume(){
        ((MainActivity)getActivity()).PopulateContactsList();

        super.onResume();
    }
}
