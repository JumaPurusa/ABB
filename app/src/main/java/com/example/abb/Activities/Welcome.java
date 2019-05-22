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

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("introPrefs", MODE_PRIVATE);
        final boolean isIntroScreenAlready = sharedPreferences.getBoolean("isIntroAvailable", false);
        //final String userDetails = sharedPreferences.getString("user", null);

       // iconImage = findViewById(R.id.icon);

        //Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        //iconImage.startAnimation(animation);

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }

}
