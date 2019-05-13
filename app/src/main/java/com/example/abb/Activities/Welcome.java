package com.example.abb.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.example.abb.MainActivity;
import com.example.abb.R;

public class Welcome extends AppCompatActivity {
    private ImageView iconImage;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        //window.setStatusBarColor(Color.parseColor("#FFFFF6"));

        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("introPrefs", MODE_PRIVATE);
        final boolean isIntroScreenAlready = sharedPreferences.getBoolean("isIntroAvailable", false);
        //final String userDetails = sharedPreferences.getString("user", null);

        iconImage = findViewById(R.id.icon);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        iconImage.startAnimation(animation);

        runnable = new Runnable() {
            @Override
            public void run() {

                if(isIntroScreenAlready){
                    startActivity(new Intent(Welcome.this, Login.class));
                    finish();
                }else{
                    startActivity(new Intent(Welcome.this, IntoScreen.class));
                    finish();
                }
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 2000);


        /*
        Thread timer = new Thread(){

            @Override
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

                    if(isIntroScreenalready){
                        startActivity(new Intent(Welcome.this, Login.class));
                        finish();
                    }else{

                        startActivity(new Intent(Welcome.this, IntoScreen.class));
                        finish();
                    }
                }
            }
        };

        timer.start();
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }

}
