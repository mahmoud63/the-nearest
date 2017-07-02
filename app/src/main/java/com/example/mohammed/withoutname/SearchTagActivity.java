package com.example.mohammed.withoutname;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchTagActivity extends AppCompatActivity {
    double longitude,latitude;
    String City;
    float Distance;
    ArrayList<PublicPlaces> arrayList=new ArrayList <>();
    ArrayList<PublicPlaces> arrayList2=new ArrayList <>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_tag);
        MyLocation(this);
        SearchText("",this);



    }
    public float GetDistance(double lat,double lon)
    {
        Location loc1 = new Location("");
        loc1.setLatitude(latitude);
        loc1.setLongitude(longitude);
        Location loc2 = new Location("");
        loc2.setLatitude(lat);
        loc2.setLongitude(lon);
        float distanceInMeters = loc1.distanceTo(loc2);
        return Math.round(distanceInMeters/1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ListView listView=(ListView)findViewById(R.id.TagList);
                String space=" ";
                if(query.isEmpty()||query.equals(space))
                {
                    listView.setAdapter(new CustomListAdapter(SearchTagActivity.this, arrayList));
                }
                else
                {
                    for (int i=0;i<arrayList.size();i++)
                    {
                        if(arrayList.get(i).Tag.contains(query))
                        {
                            arrayList2.add(arrayList.get(i));
                        }
                    }
                    if(arrayList2.size()==0)
                    {
                        listView.setAdapter(null);
                    }
                    else {
                        listView.setAdapter(new CustomListAdapter(SearchTagActivity.this, arrayList2));
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
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
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(2));
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

    public void SearchText(final String Text, final Context context)
    {
        final ListView listView=(ListView)findViewById(R.id.TagList);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Places").orderByChild("Place Category").equalTo(PublicParamaters.CategoryName.toUpperCase());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    try {
                        //Start Activity Without Search , Category Only
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                Distance = GetDistance(Double.parseDouble(""+issue.child("Place Lat").getValue()) , Double.parseDouble(""+issue.child("Place Lng").getValue()));
                               /* arrayList.add(new PublicPlaces(Distance, issue.child("Place Logo").getValue().toString(),
                                        issue.child("Place Name").getValue().toString(), issue.child("Place Description").getValue().toString(),
                                        issue.child("Place Location").getValue().toString(), issue.child("Place Website").getValue().toString(),
                                        issue.child("Place WorkHour").getValue().toString(), issue.child("Place Tags").getValue().toString(),
                                        issue.child("Place Images").getValue().toString(), issue.child("Place Phone").getValue().toString()));*/
                            arrayList.add(new PublicPlaces(issue.child("Logo").getValue() + ""
                                    , issue.child("Place Name").getValue() + ""
                                    , issue.child("Place Location").getValue() + ""
                                    , Double.parseDouble("" + issue.child("Place Lng").getValue())
                                    , Double.parseDouble(issue.child("Place Lat").getValue() + ""),Distance
                                    ,issue.child("Place Tags").getValue()+""
                                    ,issue.child("Place Description").getValue()+""
                                    ,issue.child("Place Images").getValue()+""
                                    ,issue.child("Place Phone").getValue()+""
                                    ,issue.child("Place Website").getValue()+""
                                    ,issue.child("Place WorkHour").getValue()+""
                            ));
                        }
                    }
                    catch (Exception Ex)
                    {
                        Toast.makeText(context, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SearchTagActivity.this, "Mis", Toast.LENGTH_SHORT).show();
                }
                try {
                    Toast.makeText(SearchTagActivity.this, "" + arrayList.size(), Toast.LENGTH_SHORT).show();

                    listView.setAdapter(new CustomListAdapter(SearchTagActivity.this,arrayList));
                }catch (Exception ex)
                {
                    Toast.makeText(SearchTagActivity.this,ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        final String itemValue = (String) listView.getItemAtPosition(position);
                        Toast.makeText(SearchTagActivity.this, itemValue, Toast.LENGTH_SHORT).show();
                        PublicParamaters.lat=23;
                        PublicParamaters.lon=35;
                        Intent show=new Intent(context,ShowDetailsActivity.class);
                        startActivity(show);


                    }

                });


    }
}
