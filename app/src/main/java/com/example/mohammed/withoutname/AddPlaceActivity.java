package com.example.mohammed.withoutname;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddPlaceActivity extends AppCompatActivity {
    FirebaseDatabase firebasedatabase;
    DatabaseReference mDatabase;
    List myList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        Toast.makeText(this,PublicParamaters.UserRootId, Toast.LENGTH_SHORT).show();
        myList.add("TAG1");
        myList.add("TAG2");
        myList.add("TAG3");
        firebasedatabase= FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl("https://without-name.firebaseio.com/Places/");
        DatabaseReference id = mDatabase.push();
        id.child("Place Lng").setValue(35.0);
        id.child("Place Lat").setValue(52.0);
        id.child("Place Tags").setValue(myList);
        id.child("Place Describtion").setValue("Des 1");
        id.child("Place Location").setValue("L 1");
        id.child("Place Name").setValue("P 1");
        id.child("Place Category").setValue(("Restaurant").toUpperCase());
        id.child("Place Own").setValue(PublicParamaters.UserRootId);

    }
}
