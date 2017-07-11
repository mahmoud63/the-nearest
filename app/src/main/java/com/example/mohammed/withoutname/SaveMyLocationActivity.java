package com.example.mohammed.withoutname;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaveMyLocationActivity extends AppCompatActivity {

    private TrackGPS gps;
    double longitude,latitude;
    EditText Title,Description;
    TextInputLayout textInputLayout1,textInputLayout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_my_location);
        Title=(EditText)findViewById(R.id.ET_Title);
        Description=(EditText)findViewById(R.id.ET_Description);
        Button Save=(Button)findViewById(R.id.BT_Save);
        Database.CreateDatabase(SaveMyLocationActivity.this);
        Database.CreateTableSaves("Saves");
        textInputLayout1=(TextInputLayout)findViewById(R.id.TIL_Title);
        textInputLayout2=(TextInputLayout)findViewById(R.id.TIL_Description);
        MyLocation();
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude!=0&&longitude!=0&&ValidTitle()&&ValidDescription())
                {
                    Database.InsertIntoSaves("Saves",Title.getText().toString(),Description.getText().toString()
                            ,""+longitude,""+latitude,SaveMyLocationActivity.this);
                }
                else
                {
                    Toast.makeText(SaveMyLocationActivity.this, "Pls Your GPS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

   private boolean ValidTitle()
    {
        boolean isValid = true;
        if(Title.getText().toString().isEmpty())
        {
            textInputLayout1.setError("This field is require ");
            isValid=false;
        }
        else {
            textInputLayout1.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidDescription()
    {
        boolean isValid=true;
        if(Description.getText().toString().isEmpty())
        {
            textInputLayout2.setError("This field is require ");
            isValid=false;
        }
        else
        {
            textInputLayout2.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    public void MyLocation()
    {
        gps = new TrackGPS(SaveMyLocationActivity.this);
        if(gps.canGetLocation()){
            longitude = gps.getLongitude();
            latitude = gps .getLatitude();
        }
        else
        {
            gps.showSettingsAlert();
        }
    }
}
