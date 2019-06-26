package com.example.abb.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.abb.Activities.BecomeDonor;
import com.example.abb.Activities.ChatRoom;
import com.example.abb.Activities.Notifications;
import com.example.abb.Activities.ProfileActivity;
import com.example.abb.Activities.RequestBlood;
import com.example.abb.Adapters.SlideImageAdapter;
import com.example.abb.MainActivity;
import com.example.abb.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private CardView nutritionCard, lifestyleCard, clinicCard, fitnessCard, bloodGlucoseCard;
    private ImageView menuIcon;

    boolean expandActionBar = true;
    private int dotscount;
    private ImageView[] dots;
    private LinearLayout sliderDotPanel;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        menuIcon = view.findViewById(R.id.menu_icon);

        appBarLayout = view.findViewById(R.id.appBar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                if(Math.abs(i) > 300){
                    expandActionBar = false;
                    //collapsingToolbarLayout.setTitle("AfyaPlus");
                    getActivity().invalidateOptionsMenu();
                }else{
                    expandActionBar = true;
                    //collapsingToolbarLayout.setTitle("AfyaPlus");
                    getActivity().invalidateOptionsMenu();
                }
            }
        });

        viewPager = view.findViewById(R.id.slide_page);
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(getContext());
        viewPager.setAdapter(slideImageAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

        sliderDotPanel = view.findViewById(R.id.slide_dot_panel);

        dotscount = slideImageAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i=0; i<dots.length; i++){

            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            sliderDotPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0; i<dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext()
                        , R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        view.findViewById(R.id.request_blood).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), RequestBlood.class));
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        view.findViewById(R.id.become_donor).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), BecomeDonor.class));
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        view.findViewById(R.id.chart_room).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), ProfileActivity.class));
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        view.findViewById(R.id.notifications).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), Notifications.class));
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );





    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        menuIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        DrawerLayout drawerLayout = mainActivity.getDrawer();
                        drawerLayout.openDrawer(GravityCompat.START);

                    }
                }
        );
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            try{

                getActivity().runOnUiThread(new Runnable() {
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


}
