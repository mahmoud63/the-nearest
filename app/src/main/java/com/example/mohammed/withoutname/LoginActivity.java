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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        final EditText Username=(EditText)findViewById(R.id.ET_Name);
        final EditText Password=(EditText)findViewById(R.id.Et_Password);

        Button Sign_up=(Button)findViewById(R.id.BT_Sign_up);
        Button Login=(Button)findViewById(R.id.BT_Login);

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child("Users").orderByChild("Password").equalTo("" +Password.getText());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for (DataSnapshot info : dataSnapshot.getChildren())
                            {
                                if(info.child("User Name").getValue().equals(""+Username.getText())||info.child("Email").getValue().equals(""+Username.getText()))
                                {
                                    PublicParamaters.UserInfo.add(new User(info.child("User Name").getValue().toString()
                                            ,info.child("Email").getValue().toString()
                                            ,info.child("Password").getValue().toString()
                                            ,info.child("Telephone").getValue().toString()));
                                    PublicParamaters.Write("UserId.txt",info.getKey(),LoginActivity.this);
                                    PublicParamaters.UserRootId=info.getKey();
                                    Toast.makeText(LoginActivity.this,"Done ...", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(LoginActivity.this,HomeActivityProfile.class);
                                    startActivity(intent);
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Pls Check Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
