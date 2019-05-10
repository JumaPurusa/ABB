package com.example.abb.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(Color.parseColor("#FFFFF6"));

        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("abb", MODE_PRIVATE);
        final String userDetails = sharedPreferences.getString("user", null);

        iconImage = findViewById(R.id.icon);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        iconImage.startAnimation(animation);

        Thread timer = new Thread(){

            @Override
            public void run() {

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

                    if(userDetails == null){
                        startActivity(new Intent(Welcome.this, Login.class));
                        finish();
                    }else{

                        startActivity(new Intent(Welcome.this, MainActivity.class));
                        finish();
                    }
                }
            }
        };

        timer.start();
    }
}
