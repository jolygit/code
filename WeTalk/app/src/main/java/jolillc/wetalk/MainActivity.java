package jolillc.wetalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jolillc.wetalk.data.MyContact;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends Activity {



    public void login(View view) {
        Intent intent = new Intent(this,login.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        intent.putExtra("userid", "aj");
        intent.putExtra("pwsd", "joly123");
        startActivity(intent);
    }

    private static final Uri DOCS_URI = Uri.parse(
            "http://developer.android.com/design/building-blocks/buttons.html#borderless");
    private ArrayList<MyContact> MyContacts = new ArrayList<MyContact>();
    public class StartConnection {
        public StartConnection(boolean rstart) {
            readyToStat=rstart;
        }
        public boolean readyToStat;
        public boolean Start() {
            Toast.makeText(MainActivity.this,
                    "works",
                    Toast.LENGTH_SHORT).show();
            //Log.d("alex","starting connection");
            return true;
        }
        public boolean Hangup() {
            Toast.makeText(MainActivity.this,
                    "hangup",
                    Toast.LENGTH_SHORT).show();
             return true;
        }
    }
    StartConnection st=new StartConnection(false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        super.onCreate(savedInstanceState);
        MyContacts.add(new MyContact("alex","joly",false,false));
        MyContacts.add(new MyContact("gigi","esak1",true,false));
        MyContacts.add(new MyContact("gigi","esak2",true,false));
        MyContacts.add(new MyContact("alex","joly2",false,false));
        MyContacts.add(new MyContact("gigi","esak12",true,false));
        MyContacts.add(new MyContact("gigi","esak22",true,false));
        MyContacts.add(new MyContact("gigi","esak122",true,false));
        MyContacts.add(new MyContact("gigi","esak25",true,false));
        MyContacts.add(new MyContact("alex","joly6",false,false));
        MyContacts.add(new MyContact("gigi","esak18",true,false));
        MyContacts.add(new MyContact("gigi","esak28",true,false));


        //create our new array adapter
        ArrayAdapter<MyContact> adapter = new contactArrayAdapter(this, 0, MyContacts);

        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(adapter);

    }

    class contactArrayAdapter extends ArrayAdapter<MyContact> {

        private Context context;
        private List<MyContact> MyContacts;

        //constructor, call on creation
        public contactArrayAdapter(Context context, int resource, ArrayList<MyContact> objects) {
            super(context, resource, objects);

            this.context = context;
            this.MyContacts = objects;
        }

        //called when rendering the list
        public View getView(final int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            MyContact contact = MyContacts.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            View view;
            view = inflater.inflate(R.layout.contacts_layout, null);

            TextView Name = (TextView) view.findViewById(R.id.FirstName);
            TextView LastName = (TextView) view.findViewById(R.id.LastName);
            ImageView image = (ImageView) view.findViewById(R.id.status);
            final CheckBox check=(CheckBox) view.findViewById(R.id.select);
            check.setChecked(MyContacts.get(position).getSelected());
           //run the app and look at the log as you press the checkboxes
            check.setOnCheckedChangeListener(
                    new CheckBox.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton view, boolean isChecked) {

                                Log.d("alex","checked ID"+view.getId()+" position "+position+" checked ? "+isChecked);
                            if(isChecked)
                                MyContacts.get(position).setSelected(true);
                            else
                                MyContacts.get(position).setSelected(false);

                            notifyDataSetChanged();
                            }
                    }
            );
            Name.setText(String.valueOf(contact.getfirstName()));
            LastName.setText(String.valueOf(contact.getlastName()));
            findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
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
            });

        if(MyContacts.get(position).getStatus()){

            image.setImageResource(R.drawable.ic_green);
        }
            else{
            image.setImageResource(R.drawable.ic_gray);
        }
            return view;
        }
    }
}
