package com.google.sample.echo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aj on 4/3/17.
 */
class requestsArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> MyRequests;
    public requestsArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }
    //called when rendering the list
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String contact = getItem(position);
        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //conditionally inflate either standard or special template
        View view;
        view = inflater.inflate(R.layout.requestslayout, null);
        TextView req = (TextView) view.findViewById(R.id.request);
        req.setText(String.valueOf(contact));
        view.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "accepted", Toast.LENGTH_SHORT).show();
                remove(contact);
                ((MyGlobals) context.getApplicationContext()).GetPeerRequests().remove(contact);
                int numPeerRequest=((MyGlobals) context.getApplicationContext()).GetPeerRequests().size();
                TextView cnt=(TextView)((MyGlobals) context.getApplicationContext()).GetTab().getCustomView().findViewById(R.id.count1); //(TextView)tabLayout.getTabAt(1).getCustomView().findViewById(R.id.count1);
                if(numPeerRequest>0) {
                    cnt.setText("" + numPeerRequest);
                }
                else{
                    cnt.setText("");
                    cnt.setBackgroundResource(android.R.color.transparent);
                }
                notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "rejected", Toast.LENGTH_SHORT).show();
                remove(contact);
                ((MyGlobals) context.getApplicationContext()).GetPeerRequests().remove(contact);
                int numPeerRequest=((MyGlobals) context.getApplicationContext()).GetPeerRequests().size();
                TextView cnt=(TextView)((MyGlobals) context.getApplicationContext()).GetTab().getCustomView().findViewById(R.id.count1); //(TextView)tabLayout.getTabAt(1).getCustomView().findViewById(R.id.count1);
                if(numPeerRequest>0) {
                    cnt.setText("" + numPeerRequest);
                }
                else{
                    cnt.setText("");
                    cnt.setBackgroundResource(android.R.color.transparent);
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }
}

