package com.example.abb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abb.Activities.BecomeDonor;
import com.example.abb.Activities.ChatRoom;
import com.example.abb.Activities.FeedbackActivity;
import com.example.abb.Activities.Notifications;
import com.example.abb.Activities.ProfileActivity;
import com.example.abb.Activities.RequestBlood;
import com.example.abb.Adapters.SlideImageAdapter;
import com.example.abb.Fragments.HomeFragment;
import com.example.abb.Fragments.ProfileFragment;
import com.example.abb.Model.User;
import com.example.abb.Utils.DialogDisplay;
import com.example.abb.Utils.SaveSettings;
import com.google.gson.Gson;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final static String TAG = MainActivity.class.getName();
    private Context mContext = MainActivity.this;

    public DrawerLayout drawer;
    private NavigationView navigationView;

    private SharedPreferences sharedPreferences;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);
        setSupportActionBar(toolbar);

        String userJsonString = sharedPreferences.getString("profile", null);
        if(userJsonString != null);
            user = new Gson().fromJson(userJsonString, User.class);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onDrawerProfileSet();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainContentPane, new HomeFragment())
                    .commit();
        }


    }

    private void onDrawerProfileSet(){

        View headerView = navigationView.getHeaderView(0);
        TextView fullName = headerView.findViewById(R.id.fullName);
        TextView username = headerView.findViewById(R.id.username);

        // Set student profile on drawer
        if (user == null)
            return;

        fullName.setText(user.getFirst_name() + " " + user.getLast_name());
        username.setText(user.getUsername());
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.ic_home){

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.mainContentPane, new HomeFragment());
            ft.commit();

        }else if(id == R.id.ic_profile){

            //getSupportFragmentManager().beginTransaction()
                    //.replace(R.id.mainContentPane, new ProfileFragment())
                    //.commit();
            startActivity(new Intent(this, ProfileActivity.class));


        }else if(id == R.id.ic_feedback){

            ApplicationInfo apk = getApplicationContext().getApplicationInfo();
            String apkPath = apk.sourceDir;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setData(Uri.fromFile(new File(apkPath)));
            shareIntent.setType("*/*");
            startActivity(Intent.createChooser(shareIntent, "SHARE APP USING"));


            //Intent settingsIntent = new Intent(this, FeedbackActivity.class);
            //startActivity(settingsIntent);
            //foverridePendingTransition(R.anim.fade_in, R.anim.fade_out);


        }else if(id == R.id.ic_logout){

            logout();
            /*
            Intent aboutIntent = new Intent(this, About.class);
            startActivity(aboutIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            */
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public DrawerLayout getDrawer(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        return drawerLayout;
    }




    private void logout() {
        //PhotoUploadSelectionDialog logoutDialog = new PhotoUploadSelectionDialog(mContext);
        //logoutDialog.show(getSupportFragmentManager(), "Logout Dialog");
        final AlertDialog logoutAlertDialog = DialogDisplay.logoutAlertDialog(MainActivity.this, true);

        logoutAlertDialog.findViewById(R.id.cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(logoutAlertDialog.isShowing())
                            logoutAlertDialog.dismiss();
                    }
                }
        );

    }


}
