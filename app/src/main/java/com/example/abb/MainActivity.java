package com.example.abb;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.abb.Activities.BecomeDonor;
import com.example.abb.Activities.ChatRoom;
import com.example.abb.Activities.Notifications;
import com.example.abb.Activities.RequestBlood;
import com.example.abb.Adapters.SlideImageAdapter;

import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private CardView requestBloodCard, becomeDonorCard, chartRoomCard, notificationsCard;

    boolean expandActionBar = true;
    private int dotscount;
    private ImageView[] dots;
    private LinearLayout sliderDotPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);
        setSupportActionBar(toolbar);

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
                        startActivity(new Intent(MainActivity.this, RequestBlood.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        findViewById(R.id.become_donor).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, BecomeDonor.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        findViewById(R.id.chart_room).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, ChatRoom.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        findViewById(R.id.notifications).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, Notifications.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );
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
}
