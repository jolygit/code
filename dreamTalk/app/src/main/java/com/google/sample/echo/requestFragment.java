package com.google.sample.echo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aj on 4/3/17.
 */

public class requestFragment  extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.requestfragment, container, false);
    }
    @Override
    public void onResume(){
        ((MainActivity)getActivity()).PopulateRequestsList();
        super.onResume();
    }
}
