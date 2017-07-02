package com.example.mohammed.withoutname;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddPlaceActivity extends AppCompatActivity {
    FirebaseDatabase firebasedatabase;
    DatabaseReference mDatabase;
    List myList = new ArrayList();
    double longitude,latitude;
    String City;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        Toast.makeText(this,PublicParamaters.UserRootId, Toast.LENGTH_SHORT).show();
        MyLocation(this);
        myList.add("TAG1");
        myList.add("TAG2");
        myList.add("TAG3");
        firebasedatabase= FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl("https://without-name.firebaseio.com/Places/");
        DatabaseReference id = mDatabase.push();
        id.child("Place Lng").setValue(35.0);
        id.child("Place Lat").setValue(52.0);
        id.child("Place Tags").setValue(myList);
        id.child("Place Description").setValue("Des 1");
        id.child("Place Location").setValue("L 1");
        id.child("Place Name").setValue("P 1");
        id.child("Place Category").setValue(("Restaurant").toUpperCase());
        id.child("Place Own").setValue(PublicParamaters.UserRootId);
        id.child("Place City").setValue(City);
        id.child("Place Images").setValue("");
        id.child("Place Logo").setValue("");
        id.child("Place Phone").setValue("");
        id.child("Place Website").setValue("");
        id.child("Place WorkHour").setValue(5);


    }
    public void MyLocation(Context context)
    {
        TrackGPS gps;
        gps = new TrackGPS(context);
        if(gps.canGetLocation()){


            longitude = gps.getLongitude();
            latitude = gps .getLatitude();
            City=getCompleteAddressString(latitude,longitude);
        }
        else
        {
            gps.showSettingsAlert();
        }
    }
    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < 1; i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(2));
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}
