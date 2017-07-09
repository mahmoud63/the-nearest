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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.anshul.libray.PasswordEditText;

import static com.example.mohammed.withoutname.R.id.Et_Password;
import static com.example.mohammed.withoutname.R.id.TIL_Email;
import static com.example.mohammed.withoutname.R.id.TIL_Name;
import static com.example.mohammed.withoutname.R.id.TIL_Password;
import static com.example.mohammed.withoutname.R.id.TIL_Phone;

public class SignupActivity extends AppCompatActivity {
    EditText Username,Email,Phone;
    PasswordEditText Password;
    TextInputLayout UserName,EMail,PassWord,PHone;
    FirebaseDatabase firebasedatabase;
    DatabaseReference mDatabase;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        Username=(EditText)findViewById(R.id.ET_Name);
        Email=(EditText)findViewById(R.id.ET_Email);
        Phone=(EditText)findViewById(R.id.ET_Phone);
        Password=(PasswordEditText)findViewById(Et_Password);

        UserName=(TextInputLayout)findViewById(TIL_Name);
        EMail=(TextInputLayout)findViewById(TIL_Email);
        PassWord=(TextInputLayout)findViewById(TIL_Password);
        PHone=(TextInputLayout)findViewById(TIL_Phone);


        Button Sign_up=(Button)findViewById(R.id.BT_Sign_up);

        firebasedatabase=FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl("https://round-around-6db6f.firebaseio.com/Users/");

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NameValid()&&EmailValid()&&PasswordValid()&&PhoneValid())
                {
                    try{
                        DatabaseReference id=mDatabase.push();
                        id.child("User Name").setValue(Username.getText().toString());
                        id.child("Email").setValue(Email.getText().toString());
                        id.child("Password").setValue(Password.getText().toString());
                        id.child("Telephone").setValue(Phone.getText().toString());

                        PublicParamaters.UserInfo.add(new User(Username.getText().toString(),Email.getText().toString()
                                ,Password.getText().toString(),Phone.getText().toString()));
                        PublicParamaters.Write("UserId.txt",id.getKey(),SignupActivity.this);
                        PublicParamaters.UserRootId=id.getKey();
                        Intent intent=new Intent(SignupActivity.this,HomeActivityProfile.class);
                        startActivity(intent);
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(SignupActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private boolean NameValid()
    {
        boolean isValid;
        if(Username.getText().toString().length()<5)
        {
            UserName.setError("This field must more than 5 ");
            isValid=false;
        }
        else if(Username.getText().toString().length()>20)
        {
            UserName.setError("This field must less than 20 ");
            isValid=false;
        }
        else {
            UserName.setErrorEnabled(false);
            isValid=true;
        }
        return isValid; 
    }
    private boolean EmailValid()
    {
        boolean isValid = true;
        if(Email.getText().toString().isEmpty())
        {
            EMail.setError("This field is required");
            isValid=false;
        }
        else if ((!Email.getText().toString().trim().matches(emailPattern))&&isValid)
        {
            EMail.setError("This email address is invalid");
            isValid=false;
        }
        else {
            EMail.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean PasswordValid()
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
    private boolean PhoneValid()
    {
        boolean isValid;
        if(Phone.getText().toString().isEmpty())
        {
            PHone.setError("This field is required");
            isValid=false;
        }
        else if (Phone.getText().toString().length()<11)
        {
            PHone.setError("This field is required");
            isValid=false;
        }
        else {
            PHone.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
}
