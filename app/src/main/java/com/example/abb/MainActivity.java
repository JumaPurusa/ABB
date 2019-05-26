package com.example.abb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.abb.Activities.BecomeDonor;
import com.example.abb.Activities.ChatRoom;
import com.example.abb.Activities.Login;
import com.example.abb.Activities.Notifications;
import com.example.abb.Activities.ProfileActivity;
import com.example.abb.Activities.RequestBlood;
import com.example.abb.Adapters.SlideImageAdapter;
import com.example.abb.Dialogs.LoadingDialog;
import com.example.abb.Dialogs.LogoutAlertDialog;
import com.example.abb.Dialogs.LogoutDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();
    private Context mContext = MainActivity.this;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private CardView requestBloodCard, becomeDonorCard, chartRoomCard, notificationsCard;

    boolean expandActionBar = true;
    private int dotscount;
    private ImageView[] dots;
    private LinearLayout sliderDotPanel;

    // firebase object
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isUserLogin()){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setHomeButtonEnabled(true);


        appBarLayout = findViewById(R.id.appBar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                if(Math.abs(i) > 300){
                    expandActionBar = false;
                    //collapsingToolbarLayout.setTitle("AfyaPlus");
                    //invalidateOptionsMenu();
                }else{
                    expandActionBar = true;
                    //collapsingToolbarLayout.setTitle("AfyaPlus");
                    //invalidateOptionsMenu();
                }
            }
        });

        viewPager = findViewById(R.id.slide_page);
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(getApplicationContext());
        viewPager.setAdapter(slideImageAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

        sliderDotPanel = findViewById(R.id.slide_dot_panel);

        dotscount = slideImageAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i=0; i<dots.length; i++){

            dots[i] = new ImageView(getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            sliderDotPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0; i<dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                        , R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        findViewById(R.id.request_blood).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, RequestBlood.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        findViewById(R.id.become_donor).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, BecomeDonor.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        findViewById(R.id.chart_room).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, ChatRoom.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        findViewById(R.id.notifications).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, Notifications.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        setupFirebaseAuth();
    }

    private boolean isUserLogin(){
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("introPrefs", MODE_PRIVATE);

        return preferences.getBoolean("isLoggedIn", false);
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            try{

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(viewPager.getCurrentItem() == 0){
                            viewPager.setCurrentItem(1);
                        }else if(viewPager.getCurrentItem() == 1){
                            viewPager.setCurrentItem(2);
                        }else if(viewPager.getCurrentItem() == 2){
                            viewPager.setCurrentItem(3);
                        }else{
                            viewPager.setCurrentItem(0);
                        }
                    }
                });

            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.ic_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;

            case R.id.ic_logout:
                logout();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void logout() {
        //LogoutDialog logoutDialog = new LogoutDialog(mContext);
        //logoutDialog.show(getSupportFragmentManager(), "Logout Dialog");
        LogoutAlertDialog.logoutDialog(MainActivity.this, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    /**
     * setup Firebase Authentication
     */
    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){

                }else{
                    Log.d(TAG, "onAuthStateChanged: sign out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }
}
