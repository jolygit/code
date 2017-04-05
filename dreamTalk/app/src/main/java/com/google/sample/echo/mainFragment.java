package com.google.sample.echo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.sample.echo.data.MyContact;

/**
 * Created by aj on 3/19/17.
 */
public class mainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_main, container, false);
        return v;
    }
    @Override
    public void onResume(){
        ((MainActivity)getActivity()).PopulateContactsList();
        super.onResume();
    }
}

