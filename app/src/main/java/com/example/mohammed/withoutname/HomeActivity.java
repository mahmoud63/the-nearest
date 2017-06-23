package com.example.mohammed.withoutname;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class HomeActivity extends AppCompatActivity {

    CircleMenu circleMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       circleMenu=(CircleMenu)findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.home, R.drawable.home2);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.drawable.i1)
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.i2)
                .addSubMenu(Color.parseColor("#FF4B32"), R.drawable.i3);

        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

            @Override
            public void onMenuSelected(int index) {
                switch (index) {
                    case 0:
                        Toast.makeText(HomeActivity.this, "Add Place Activity", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(HomeActivity.this, "Login / Sign up Activity ", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(HomeActivity.this, "Save My Location Activity", Toast.LENGTH_SHORT).show();
                        break;
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
