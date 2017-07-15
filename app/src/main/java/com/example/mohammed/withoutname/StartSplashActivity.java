package com.example.mohammed.withoutname;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class StartSplashActivity extends AppCompatActivity {

    private GifImageView gifImageView;
    private View view1,view2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        gifImageView = (GifImageView) findViewById(R.id.Gif);
        view1=(View)findViewById(R.id.V_1);
        view2=(View)findViewById(R.id.V_2);

        try {
            InputStream inputStream=getAssets().open("GIF2.gif");
            byte []bytes= IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();

        } catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        view1.setBackgroundColor(Color.parseColor("#9ad5e3"));
        view2.setBackgroundColor(Color.parseColor("#68abb3"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PublicParamaters.Read("UserId.txt",StartSplashActivity.this).isEmpty())
                {
                    PublicParamaters.UserRootId=PublicParamaters.Read("UserId.txt",StartSplashActivity.this);
                    startActivity(new Intent(StartSplashActivity.this,HomeActivityProfile.class));
                    StartSplashActivity.this.finish();
                }
                else {
                    startActivity(new Intent(StartSplashActivity.this,HomeLoginActivity.class));
                    StartSplashActivity.this.finish();
                }
            }
        },3600);


        /*EasySplashScreen easySplashScreen =new EasySplashScreen(StartSplashActivity.this)
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

        setContentView(view);*/

    }
}
