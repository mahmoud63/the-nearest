package com.example.mohammed.withoutname;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        RetrivePublic("gg",ProfileActivity.this);
        RetriveLocal();

    }
    public void Logout(View view)
    {
        PublicParamaters.UserRootId=null;
        PublicParamaters.UserInfo.clear();
        PublicParamaters.Write("UserId.txt","",ProfileActivity.this);
        Intent intent=new Intent(ProfileActivity.this,HomeLoginActivity.class);
        startActivity(intent);
    }
    public void RetrivePublic(final String Text, final Context context)
    {
        final ListView listView=(ListView)findViewById(R.id.LV_Public);
        final ArrayList<PublicPlaces> arrayList=new ArrayList <>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").orderByChild("Password");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            arrayList.add(new PublicPlaces(issue.child("Password").getValue() + ""
                                    , issue.child("User Name").getValue() + ""
                                    , issue.child("Telephone").getValue() + ""
                                    , Double.parseDouble(""+36.5)
                                    , Double.parseDouble(""+69.14),2));
                    }
                }
                else {
                    Toast.makeText(ProfileActivity.this, "Mis", Toast.LENGTH_SHORT).show();
                }
                listView.setAdapter(new CustomAdapter(ProfileActivity.this,arrayList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void RetriveLocal()
    {
        final ListView listView;
        final ArrayList<MyPlacesDetails>arrayList;
        int Id;
        String Title,Description,Lat,lon;
        listView = (ListView) findViewById(R.id.LV_Special);
        arrayList=Database.RetrivePlaces(this);


        ArrayList<MyPlacesDetails> values = new ArrayList<>();
        values = Database.RetrivePlaces(this);

        final ArrayList<String> jj=new ArrayList<>();
        for(int i=0;i<values.size();i++)
        {
            jj.add(values.get(i).title);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,jj);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        final String itemValue = (String) listView.getItemAtPosition(position);
                        int index= jj.indexOf(itemValue);
                        final int Id=arrayList.get(index).id;
                        String Title=arrayList.get(index).title;
                        String Description=arrayList.get(index).description;
                        final String Lat=arrayList.get(index).latitude;
                        final String lon=arrayList.get(index).longitude;


                        //PopUp

                        LayoutInflater factory = LayoutInflater.from(ProfileActivity.this);
                        final View deleteDialogView = factory.inflate(R.layout.popup, null);
                        final AlertDialog deleteDialog =
                                new AlertDialog.Builder(ProfileActivity.this).create();
                        deleteDialog.setView(deleteDialogView);

                        //Connect to componanent in popup
                        TextView ti=(TextView) deleteDialogView.findViewById(R.id.ET_Title);
                        ti.setText(Title);
                        TextView des=(TextView) deleteDialogView.findViewById(R.id.ET_Describtion);
                        des.setText(Description);
                        //go button on popup
                        deleteDialogView.findViewById(R.id.BT_Go).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Toast.makeText(ProfileActivity.this, "1", Toast.LENGTH_SHORT).show();
                                    PublicParamaters.lon=Double.parseDouble(lon);
                                    PublicParamaters.lat=Double.parseDouble(Lat);
                                    Intent m=new Intent(ProfileActivity.this,MapsActivity.class);
                                    startActivity(m);
                                    deleteDialog.dismiss();
                                }catch (Exception ex)
                                {
                                    Toast.makeText(ProfileActivity.this,ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        //delete button on popup
                        deleteDialogView.findViewById(R.id.BT_delete).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Toast.makeText(ProfileActivity.this, "Here", Toast.LENGTH_SHORT).show();
                                    Database.DeletePlace(Id,ProfileActivity.this);
                                    deleteDialog.dismiss();
                                }catch (Exception ex)
                                {
                                    Toast.makeText(ProfileActivity.this, ex.getMessage()+"  2", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        deleteDialog.show();

                    }

                });


    }
}
