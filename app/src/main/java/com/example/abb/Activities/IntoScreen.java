package com.example.abb.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.abb.Adapters.IntroScreenPageAdapter;
import com.example.abb.Model.IntroItem;
import com.example.abb.R;

import java.util.ArrayList;
import java.util.List;

public class IntoScreen extends AppCompatActivity {

    private ViewPager introScreenViewPager;
    private IntroScreenPageAdapter introScreenPageAdapter;
    private List<IntroItem> introItems;

    private TabLayout tabIndicator;
    private Button btnNext, btnGetStarted;
    private TextView skipText;

    int screePostion = 0;

    private SharedPreferences screenPrefes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // When this activity is about to lunch we need to check if it is already passed
        if(isIntroAvailable()){
            Intent registrationIntent = new Intent(getApplicationContext(), Registration.class);
            startActivity(registrationIntent);
            finish();
        }

        setContentView(R.layout.activity_intro_screen);

        screenPrefes = getApplicationContext().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        introScreenViewPager = findViewById(R.id.intro_scree_view_pager);

        int[] images = {
                R.drawable.blood_request,
                R.drawable.blood_donor,
                R.drawable.chart_blood
        };

        String[] introTitles = {
                getString(R.string.request_blood),
                getString(R.string.become_donor),
                getString(R.string.chart_room)
        };

        String[] intro_descs = {
                getString(R.string.intro_description),
                getString(R.string.intro_description),
                getString(R.string.intro_description)
        };

        introItems = new ArrayList<>();
        for(int i=0; i<images.length; i++){

            IntroItem introItem = new IntroItem();
            introItem.setImage(images[i]);
            introItem.setIntroTitle(introTitles[i]);
            introItem.setIntroDesc(intro_descs[i]);
            introItems.add(introItem);
        }

        introScreenPageAdapter = new IntroScreenPageAdapter(getApplicationContext(), introItems);
        introScreenViewPager.setAdapter(introScreenPageAdapter);

        tabIndicator = findViewById(R.id.tabIndicator);
        tabIndicator.setupWithViewPager(introScreenViewPager);

        btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(nextButtonClickListener);

        // when another tab is selected
        tabIndicator.addOnTabSelectedListener(
                new TabLayout.BaseOnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if(tab.getPosition() == introItems.size() - 1)
                            loadLastScreen();
                        else{
                            unloadLastScreen();
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );


        // Get started button click listener
        btnGetStarted = findViewById(R.id.get_started);
        btnGetStarted.setOnClickListener(getStartedBtnClickListener);

        skipText = findViewById(R.id.skip);
        skipText.setOnClickListener(getStartedBtnClickListener);
    }

    private View.OnClickListener nextButtonClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            screePostion = introScreenViewPager.getCurrentItem();

            if(screePostion < introItems.size() - 1){
                screePostion++;
                introScreenViewPager.setCurrentItem(screePostion);
            }else if(screePostion == introItems.size() - 1 /*when we reach the last screen*/) {

                // show the get started button
                // hide the indicator and next button;
                loadLastScreen();
            }

        }
    };

    // Get started button handler
    private View.OnClickListener getStartedBtnClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent registrationIntent =
                    new Intent(getApplicationContext(), Registration.class);
            startActivity(registrationIntent);

            // We need to save a boolean value to Storage so next time when the user run the app
            // we could know that he is already checked the intro screens activity;
            // We are going to use sharedPreferences for that process
            saveIntroData();
            finish();
        }
    };

    private boolean isIntroAvailable(){
        SharedPreferences preferences = getApplicationContext().
                getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        return preferences.getBoolean("isIntroAvailable", false);
    }

    private void saveIntroData() {
        SharedPreferences.Editor prefs = screenPrefes.edit();
        prefs.putBoolean("isIntroAvailable", true);
        prefs.apply();
    }

    private void loadLastScreen() {

        tabIndicator.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        skipText.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);

        // create animation for the getStarted button
        Animation btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.get_started_button_animation);
        btnGetStarted.setAnimation(btnAnimation);
    }

    private void unloadLastScreen(){
        btnGetStarted.setVisibility(View.INVISIBLE);
        skipText.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
    }
}
