package com.example.mohammed.withoutname;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
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
    EditText SearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_tag);


        MyLocation(this);

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                Toast.makeText(SearchTagActivity.this,newText, Toast.LENGTH_SHORT).show();
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
                    strReturnedAddress.append(returnedAddress.getAddressLine(0))
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

    public void jku(final String Text, final Context context)
    {
        final ListView listView=(ListView)findViewById(R.id.TagList);
        final ArrayList<PublicPlaces> arrayList=new ArrayList <>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Places").orderByChild("Place Lng").startAt(25.0).endAt(26.0);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        if (Double.parseDouble(issue.child("Place Lat").getValue() + "")>=56
                                &&Double.parseDouble(issue.child("Place Lat").getValue() + "")<=57
                                && issue.child("Place Tags").getValue().toString().contains(Text)) {
                            arrayList.add(new PublicPlaces(issue.child("Logo").getValue() + ""
                                    , issue.child("Place Name").getValue() + ""
                                    , issue.child("Place Location").getValue() + ""
                                    , Double.parseDouble("" + issue.child("Place Lng").getValue())
                                    , Double.parseDouble(issue.child("Place Lat").getValue() + "")));
                        }
                    }
                }
                else {
                    Toast.makeText(SearchTagActivity.this, "Mis", Toast.LENGTH_SHORT).show();
                }
                listView.setAdapter(new CustomAdapter(SearchTagActivity.this,arrayList));
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

                       /* Intent show=new Intent(context,ShowDetailsActivity.class);
                        startActivity(show);*/


                    }

                });


    }
}
