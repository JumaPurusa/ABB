package com.example.abb.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.abb.Activities.Login;
import com.example.abb.R;

public class LogoutAlertDialog {

    public static AlertDialog logoutDialog(final Activity activity, boolean isCancelable){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.fragment_logout_dialog, null);

        builder.setView(view);

        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }


                }
        );

        confirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog builderDialog = LoadingDialog.progressDialog(activity, "Logging out ...", false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                SharedPreferences preferences = activity.getSharedPreferences("introPrefs", activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedIn", false);
                                editor.apply();

                                if(activity != null)
                                    builderDialog.dismiss();
                                    activity.startActivity(new Intent(activity, Login.class));
                                //activity.finish();

                            }
                        }, 3000);
                    }
                }
        );

        builder.setCancelable(isCancelable);

        //builder.show().getWindow().getAttributes().windowAnimations = R.style.DialogScale;

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogScale;
        dialog.show();

        return (AlertDialog)dialog;
    }


}
