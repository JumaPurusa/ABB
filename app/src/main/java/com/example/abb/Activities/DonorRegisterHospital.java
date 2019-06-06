package com.example.abb.Activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.abb.MainActivity;
import com.example.abb.Model.Hospital;
import com.example.abb.R;
import com.example.abb.Utils.Actions;

public class DonorRegisterHospital extends AppCompatActivity {

    private static final String TAG = DonorRegisterHospital.class.getName();
    private Context mContext = DonorRegisterHospital.this;

    private TextView textView, phoneText;
    private Button doneButton;

    Hospital hospital = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_register_hospital);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Information");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        if (getIntent().hasExtra("Hospital")) {

            hospital = getIntent().getParcelableExtra("Hospital");
        }


        textView = findViewById(R.id.informationText);
        textView.setText(getString(R.string.hospital_information, hospital.getHospitalName()));
        textView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        findViewById(R.id.moreInformation)
        .setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        phoneText = findViewById(R.id.phoneText);
        phoneText.setText(getString(R.string.phone_details, hospital.getHospitalContacts()));
        phoneText.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        phoneText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(getString(R.string.contact_dialog_string,
                                hospital.getHospitalName(), hospital.getHospitalContacts()));

                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onCallingAction(hospital);
                                     }
                                });

                        builder.setNegativeButton("CANCEL", null);

                        Dialog dialog = builder.create();
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogScale;
                        dialog.show();
                    }
                }
        );

        findViewById(R.id.thanks)
                .setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        findViewById(R.id.saveLife)
                .setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        doneButton = findViewById(R.id.doneButton);
        doneButton.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.get_started_button_animation));

        doneButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(DonorRegisterHospital.this, MainActivity.class);
                        //mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finishAffinity();
                    }
                }
        );

    }

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

    private void onCallingAction(Hospital hospital){

        try{

            Intent callIntent = new Intent(Intent.ACTION_DIAL
                    , Uri.parse("tel:" + hospital.getHospitalContacts()));

            startActivity(callIntent);

        }catch (ActivityNotFoundException e){
            Snackbar.make(
                findViewById(R.id.coordinatorLayout),
                e.getMessage(),
                3000
            ).show();
        }

    }
}
