package jolillc.wetalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.LoginFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends Activity {

    public void register(View view) {
        Intent intent = new Intent(this,registration.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        intent.putExtra("userid", "aj");
        intent.putExtra("pwsd", "joly123");
        startActivity(intent);
    }
    public void login(View view) {
        EditText UserName = (EditText) findViewById(R.id.usernameEntry);
        String unameSt = UserName.getText().toString();
        EditText Password = (EditText) findViewById(R.id.passwordEntry);
        String passwdSt = Password.getText().toString();
       // Toast.makeText(login.this,"incorrect"+unameSt+passwdSt,Toast.LENGTH_SHORT).show();
        if(unameSt.equals("vova") && passwdSt.equals("vova")){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(login.this,"incorrect username or password try again",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Intent intent = getIntent();
        String message = intent.getStringExtra("userid");
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_login);
        layout.addView(textView);*/
    }
}
