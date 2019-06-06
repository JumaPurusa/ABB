package com.example.abb.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abb.Model.Donor;
import com.example.abb.R;
import com.example.abb.Utils.Actions;

public class DonorDetailsActivity extends AppCompatActivity {

    private final static String TAG = DonorDetailsActivity.class.getName();
    private Activity activity = DonorDetailsActivity.this;

    private CoordinatorLayout coordinatorLayout;

    private FrameLayout callLayout, smsLayout, emailLayout;

    Donor donor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_donor_details);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Donor");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        try{

            if(getIntent().hasExtra("Donor")) {
                donor = getIntent().getParcelableExtra("Donor");
            }

            onSettingDonorDetails();

            onSettingActionClick();

        }catch (NullPointerException e){
            Toast.makeText(activity, "Donor details not available", Toast.LENGTH_SHORT).show();
        }
        // check if the intent has a message (Donor object)

    }

    private void onSettingDonorDetails(){

        ((TextView)findViewById(R.id.nameText)).setText(donor.getName());
        ((TextView)findViewById(R.id.phoneNumberText)).setText(donor.getPhone_no());
        ((TextView)findViewById(R.id.emailText)).setText(donor.getEmail());
        ((TextView)findViewById(R.id.bloodGroupText)).setText(donor.getBlood_group());
    }

    private void onSettingActionClick(){

        findViewById(R.id.callLayout).
                setOnClickListener(actionClickListener);

        findViewById(R.id.msgLayout).
                setOnClickListener(actionClickListener);

        findViewById(R.id.emailLayout).
                setOnClickListener(actionClickListener);
    }

    private View.OnClickListener actionClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.callLayout:
                    //Toast.makeText(mContext, "calling " + donor.getPhone_no(), Toast.LENGTH_SHORT).show();
                    // calling donor action
                    Actions.onCallingDonor(activity, donor,
                            coordinatorLayout);
                    break;

                case R.id.msgLayout:
                    //Toast.makeText(mContext, "messaging " + donor.getPhone_no(), Toast.LENGTH_SHORT).show();
                    // messaging donor action
                    Actions.onMessagingDonor(activity, donor,
                            coordinatorLayout);
                    break;

                case R.id.emailLayout:
                    //Toast.makeText(mContext, "Email " + donor.getEmail(), Toast.LENGTH_SHORT).show();
                    // emailing donor action
                    Actions.onEmailingDonor(activity, donor,
                            coordinatorLayout);
                    break;

                    default:
                        Toast.makeText(activity, "Please choose an action", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
