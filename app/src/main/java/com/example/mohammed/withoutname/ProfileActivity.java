package com.example.mohammed.withoutname;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {


    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        RetriveLocal();
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RetriveLocal();
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RetrivePublic(PublicParamaters.UserRootId,ProfileActivity.this);
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Logout();
                materialDesignFAM.close(true);
            }
        });
    }


    public void RetrivePublic(final String Text, final Context context)
    {
        try {
            final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
            final ArrayList<PublicPlaces> arrayList = new ArrayList<>();
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("Places").orderByChild("Place Own").equalTo(Text);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            arrayList.add(new PublicPlaces(
                                            issue.child("Place Logo").child("url").getValue() .toString()
                                            , issue.child("Place Name").getValue() + ""
                                            , issue.getKey() + ""
                                    )
                            );
                            Toast.makeText(context, issue.child("Place Logo").child("url").getValue() .toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Mis", Toast.LENGTH_SHORT).show();
                    }
                    listView.setAdapter(new CustomAdapter(ProfileActivity.this, arrayList));
                 //From Here


                    SwipeMenuCreator swipeMenuCreator=new SwipeMenuCreator() {
                        @Override
                        public void create(SwipeMenu menu) {
                            //create an action that will be showed on swiping an item in the list
                            SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                            item1.setWidth(135);
                            item1.setTitle("Show");
                            item1.setTitleSize(18);
                            item1.setIcon(R.drawable.ic_visibility_black_24dp);
                            item1.setTitleColor(Color.BLACK);
                            menu.addMenuItem(item1);

                            SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                            // set item background
                            item2.setWidth(135);
                            item2.setTitle("Edit");
                            item2.setTitleSize(18);
                            item2.setIcon(R.drawable.ic_create_black_24dp);
                            item2.setTitleColor(Color.BLACK);
                            menu.addMenuItem(item2);

                            SwipeMenuItem item3 = new SwipeMenuItem(getApplicationContext());
                            // set item background
                            item3.setWidth(135);
                            item3.setTitle("Delete");
                            item3.setTitleSize(18);
                            item3.setIcon(R.drawable.ic_delete_black_24dp);
                            item3.setTitleColor(Color.BLACK);
                            menu.addMenuItem(item3);
                        }
                    };

                    listView.setMenuCreator(swipeMenuCreator);
                    listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);


                    listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                            switch (index){
                                case 0:
                                    Toast.makeText(ProfileActivity.this, "1"+arrayList.get(position).Name, Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    PublicParamaters.PlaceRootId=arrayList.get(position).RootId;
                                    Intent intent=new Intent(ProfileActivity.this,EditActivity.class);
                                    startActivity(intent);
                                     break;
                                case 2:
                                    int po=position;
                                    reference.child("Places").child(arrayList.get(po).RootId).removeValue();
                                    arrayList.remove(po);
                                    listView.setAdapter(new CustomAdapter(ProfileActivity.this, arrayList));
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });



                    //TO Here

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void Logout()
    {
        PublicParamaters.UserRootId=null;
        PublicParamaters.UserInfo.clear();
        PublicParamaters.Write("UserId.txt","",ProfileActivity.this);
        Intent intent=new Intent(ProfileActivity.this,HomeLoginActivity.class);
        startActivity(intent);
    }
    public void RetriveLocal()
    {
        final SwipeMenuListView listView;
        final ArrayList<MyPlacesDetails>arrayList;
        listView = (SwipeMenuListView) findViewById(R.id.listView);
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
//Swipe null
        listView.setMenuCreator(null);
        listView.setSwipeDirection(AbsListView.CHOICE_MODE_NONE);




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
                                new AlertDialog.Builder(ProfileActivity.this)
                                        .create();
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
                                    adapter.remove(itemValue);
                                    adapter.notifyDataSetChanged();
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
