package com.google.sample.echo;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aj on 3/24/17.
 */
public class chatArrayAdapter extends ArrayAdapter<String> {

private Context context;
//private List<String> MyChats;

//constructor, call on creation
public chatArrayAdapter(Context context, int resource) {
        super(context, resource);

        this.context = context;
        //this.MyChats = objects;
        }

//called when rendering the list

@Override
public View getView(final int position, View convertView, ViewGroup parent) {
        ConstraintSet set = new ConstraintSet();
        //get the property we are displaying
        String chat = getItem(position);
        String[] cht=chat.split("abdulmanatkhabib:");//this is done to distinguish incoming text from your own one! That way text is printed to the left or to the right of the window

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //conditionally inflate either standard or special template



        if(cht.length==2) {
            View view;
            view = inflater.inflate(R.layout.chattext, null);
            TextView tv = (TextView) view.findViewById(R.id.texttosendthem1);
            tv.setText(cht[1]);
            return view;

        }
        else {
            View view;
            view = inflater.inflate(R.layout.chattext1, null);
            TextView tv = (TextView) view.findViewById(R.id.texttosendme1);
            tv.setText(cht[0]);
            return view;
        }

    }
}


