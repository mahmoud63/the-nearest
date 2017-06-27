package com.example.mohammed.withoutname;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    FirebaseDatabase firebasedatabase;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        final EditText Username=(EditText)findViewById(R.id.ET_Name);
        final EditText Email=(EditText)findViewById(R.id.ET_Email);
        final EditText Password=(EditText)findViewById(R.id.Et_Password);
        final EditText Telephone=(EditText)findViewById(R.id.Et_Telephone);
        Button Sign_up=(Button)findViewById(R.id.BT_Sign_up);

        firebasedatabase=FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl("https://without-name.firebaseio.com/Users/");

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                DatabaseReference id=mDatabase.push();
                id.child("User Name").setValue(Username.getText().toString());
                id.child("Email").setValue(Email.getText().toString());
                id.child("Password").setValue(Password.getText().toString());
                id.child("Telephone").setValue(Telephone.getText().toString());
                    PublicParamaters.UserInfo.add(new User(Username.getText().toString(),Email.getText().toString()
                    ,Password.getText().toString(),Telephone.getText().toString()));
                    PublicParamaters.Write("UserId.txt",id.getKey(),SignupActivity.this);
                    PublicParamaters.UserRootId=id.getKey();
                Toast.makeText(SignupActivity.this,"Done ...", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SignupActivity.this,HomeActivityProfile.class);
                    startActivity(intent);
            }
            catch (Exception ex)
            {
                Toast.makeText(SignupActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            }
        });

    }
}
