package com.example.mohammed.withoutname;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class StartSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EasySplashScreen easySplashScreen =new EasySplashScreen(StartSplashActivity.this)
                .withFullScreen()
                .withSplashTimeOut(4000).withLogo(R.drawable.home)
                .withBackgroundResource(R.drawable.background)
                .withHeaderText("Ezzat")
                .withFooterText("Ouda").withBeforeLogoText("Before Logo").withAfterLogoText("After Logo");

        easySplashScreen.getHeaderTextView().setTextColor(Color.WHITE);
        easySplashScreen.getAfterLogoTextView().setTextColor(Color.WHITE);
        easySplashScreen.getBeforeLogoTextView().setTextColor(Color.WHITE);
        easySplashScreen.getFooterTextView().setTextColor(Color.WHITE);

        if (!PublicParamaters.Read("UserId.txt",this).isEmpty())
        {
            PublicParamaters.UserRootId=PublicParamaters.Read("UserId.txt",this);
            easySplashScreen.withTargetActivity(HomeActivityProfile.class);
        }
        else {
            easySplashScreen.withTargetActivity(HomeLoginActivity.class);
        }

        View view =easySplashScreen.create();

        setContentView(view);

    }
}
