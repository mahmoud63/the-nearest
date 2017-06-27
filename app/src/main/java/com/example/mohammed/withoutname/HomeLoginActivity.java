package com.example.mohammed.withoutname;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class HomeLoginActivity extends AppCompatActivity {
    CircleMenu circleMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);

        //PublicParamaters.Write("","ezzat",this);

        circleMenu=(CircleMenu)findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.home, R.drawable.home2);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.drawable.i1)
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.home)
                .addSubMenu(Color.parseColor("#FF4B32"), R.drawable.i3);

        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

                                                 @Override
                                                 public void onMenuSelected(int index) {
                                                     switch (index) {
                                                         case 0:
                                                             Handler handler = new Handler();
                                                             handler.postDelayed(new Runnable() {
                                                                 public void run() {
                                                                     Intent intent=new Intent(HomeLoginActivity.this,AddPlaceActivity.class);
                                                                     startActivity(intent);
                                                                 }
                                                             }, 1500);
                                                             break;
                                                         case 1:
                                                             Handler handler1 = new Handler();
                                                             handler1.postDelayed(new Runnable() {
                                                                 public void run() {
                                                                     Intent intent=new Intent(HomeLoginActivity.this,LoginActivity.class);
                                                                     startActivity(intent);
                                                                 }
                                                             }, 1500);                        break;
                                                         case 2:
                                                             Handler handler2 = new Handler();
                                                             handler2.postDelayed(new Runnable() {
                                                                 public void run() {
                                                                     Intent intent=new Intent(HomeLoginActivity.this,SaveMyLocationActivity.class);
                                                                     startActivity(intent);
                                                                 }
                                                             }, 1500);                        break;
                                                     }
                                                 }

                                             }

        );
    }
    @Override
    public void onBackPressed() {
        if (circleMenu.isOpened())
            circleMenu.closeMenu();
        else
            finish();
    }

}
