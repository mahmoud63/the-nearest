package com.example.mohammed.withoutname;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

import in.anshul.libray.PasswordEditText;

public class LoginActivity extends AppCompatActivity {
    boolean Found ;
    TextInputLayout UserName,PassWord;
    PasswordEditText Password;
    EditText Username;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Password=(PasswordEditText)findViewById(R.id.Et_Password);
        Username=(EditText)findViewById(R.id.ET_Name);

        UserName=(TextInputLayout)findViewById(R.id.TIL_Name);
        PassWord=(TextInputLayout)findViewById(R.id.TIL_Password);

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
                if(ValidUserEmail()&&ValidPassword())
                {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference.child("Users").orderByChild("Email").equalTo("" +Username.getText());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                for (DataSnapshot info : dataSnapshot.getChildren())
                                {
                                    Found=false;
                                    if(info.child("Password").getValue().equals(Password.getText().toString()))
                                    {
                                        Found=true;
                                        PublicParamaters.UserInfo.add(new User(info.child("User Name").getValue().toString()
                                                ,info.child("Email").getValue().toString()
                                                ,info.child("Password").getValue().toString()
                                                ,info.child("Telephone").getValue().toString()));
                                        PublicParamaters.Write("UserId.txt",info.getKey(),LoginActivity.this);
                                        PublicParamaters.UserRootId=info.getKey();
                                        Intent intent=new Intent(LoginActivity.this,HomeActivityProfile.class);
                                        startActivity(intent);
                                        break;
                                    }
                                }
                                if(!Found)
                                {
                                    Toast.makeText(LoginActivity.this, "Pls check password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Pls check email", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
    private boolean ValidUserEmail()
    {
        boolean isValid=true;
        if(Username.getText().toString().isEmpty())
        {
            UserName.setError("This field is required");
            isValid=false;
        }
        else if ((!Username.getText().toString().trim().matches(emailPattern))&&isValid)
        {
            UserName.setError("This email address is invalid");
            isValid=false;
        }
        else {
            UserName.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidPassword()
    {
        boolean isValid;
        if(Password.getText().toString().length()<5)
        {
            PassWord.setError("This field must more than 5 ");
            isValid=false;
        }

        else {
            PassWord.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
}
