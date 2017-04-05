package com.google.sample.echo;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.google.sample.echo.data.MyContact;
class contactArrayAdapter extends ArrayAdapter<MyContact> {
    private Context context;
    private List<MyContact> MyContacts;
    public contactArrayAdapter(Context context, int resource, ArrayList<MyContact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.MyContacts = objects;
    }
    //called when rendering the list
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyContact contact = MyContacts.get(position);
        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //conditionally inflate either standard or special template
        View view;
        view = inflater.inflate(R.layout.contacts_layout, null);
        TextView Name = (TextView) view.findViewById(R.id.FirstName);
        ImageView image = (ImageView) view.findViewById(R.id.status);
        final CheckBox check=(CheckBox) view.findViewById(R.id.select);
        check.setChecked(MyContacts.get(position).getSelected());
        check.setOnCheckedChangeListener(
                new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                        if(isChecked)
                            MyContacts.get(position).setSelected(true);
                        else
                            MyContacts.get(position).setSelected(false);
                        notifyDataSetChanged();
                    }
                }
        );
        Name.setText(String.valueOf(contact.getUsername())); //tmp hack
        /*findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st.Start();
                Log.d("alex","calling...");

            }
        });
        findViewById(R.id.hangup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st.Hangup();
            }
        });*/
        if(MyContacts.get(position).getStatus()){
            image.setImageResource(R.drawable.ic_green);
        }
        else{
            image.setImageResource(R.drawable.ic_gray);
        }
        return view;
    }
}
