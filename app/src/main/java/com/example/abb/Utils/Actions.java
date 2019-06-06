package com.example.abb.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.example.abb.Model.Donor;
import com.example.abb.R;

public class Actions {

    public static void onCallingDonor(Activity activity, Donor donor, CoordinatorLayout layout){
        try{

            //Intent callIntent = new Intent(Intent.ACTION_CALL
                   // , Uri.parse("tel:" + donor.getPhone_no()));
           // activity.startActivity(Intent.createChooser(callIntent,"Making a call to " + donor.getPhone_no()));

            Intent callIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + donor.getPhone_no()));
            activity.startActivity(callIntent);

        }catch (ActivityNotFoundException e){
            showSnackMessage(e.getMessage(), layout);
        }
        // this could have been done more better with Intent callIntent = new Intent(Intent.ACTION_DIAL)
    }

    public static void onMessagingDonor(Activity activity, Donor donor, CoordinatorLayout layout){

        /*
        try{

            Intent messageIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("sms:" + donor.getPhone_no()));

                 startActivity(Intent.createChooser(messageIntent, "Start messaging " +
                    donor.getPhone_no()));

        }catch (ActivityNotFoundException e){
            showSnackMessage(e.getMessage(), (CoordinatorLayout)findViewById(R.id.coordinatorLayout));
        }*/

        // this could have been done more better with Intent.ACTION_SEND or SmsManager

        Intent messageIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("sms:" + donor.getPhone_no()));

        if(messageIntent.resolveActivity(activity.getPackageManager()) != null){
            activity.startActivity(Intent.createChooser(messageIntent, "Start messaging " +
                    donor.getPhone_no()));
        }else
            showSnackMessage("There is no normal Sms Activity in your device",
                    layout);
    }

    public static void onEmailingDonor(Activity activity, Donor donor, CoordinatorLayout layout){

        try{

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + donor.getEmail()));
            activity.startActivity(Intent.createChooser(emailIntent, "Email: " + donor.getEmail()));

        }catch (ActivityNotFoundException e){
            showSnackMessage(e.getMessage(), layout);
        }
        // this could have been done more better by designing a layout for sending email

    }

    private static void showSnackMessage(String message, CoordinatorLayout layout){
        Snackbar.make(layout, message, 2000).show();
    }
}
