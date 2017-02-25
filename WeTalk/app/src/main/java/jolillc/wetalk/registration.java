package jolillc.wetalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class registration extends Activity {
    public void backtologin(View view) {
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
    }
    public void register(View view) {
        EditText fname = (EditText) findViewById(R.id.fnameEditText);
        String fnameSt = fname.getText().toString();

        EditText lname = (EditText) findViewById(R.id.lnameEditText);
        String lnameSt = lname.getText().toString();

        EditText uname = (EditText) findViewById(R.id.unameEditText);
        String unameSt = uname.getText().toString();

        EditText pswd = (EditText) findViewById(R.id.rpasswordEdit);
        String pswdSt = uname.getText().toString();

        EditText email = (EditText) findViewById(R.id.remailEditText);
        String emailSt = uname.getText().toString();

        if(unameSt.equals("vova") && pswdSt.equals("vova")){ // successful reg
            Toast.makeText(registration.this,"You have registered sucessfully! Ready to login",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,login.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(registration.this,"username exists try different one",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

    }
}
