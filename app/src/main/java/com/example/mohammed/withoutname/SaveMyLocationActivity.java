package com.example.mohammed.withoutname;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaveMyLocationActivity extends AppCompatActivity {

    private TrackGPS gps;
    double longitude,latitude;
    EditText Title,Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_my_location);
        Title=(EditText)findViewById(R.id.ET_Title);
        Description=(EditText)findViewById(R.id.ET_Description);
        Button Save=(Button)findViewById(R.id.BT_Save);
        Database.CreateDatabase(SaveMyLocationActivity.this);
        Database.CreateTableSaves("Saves");
        MyLocation();
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude!=0&&longitude!=0)
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
