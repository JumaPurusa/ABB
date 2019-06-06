package com.example.abb.Utils;

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

public class DialogDisplay {

    public static AlertDialog progressDialogWaiting(Activity activity, String message, boolean isCancelable){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_progress_waiting_dialog, null);

        dialogBuilder.setCancelable(isCancelable);

        ((TextView)view.findViewById(R.id.messageText)).setText(message);

        dialogBuilder.setView(view);

        return dialogBuilder.create();
    }

    public static AlertDialog logoutAlertDialog(final Activity activity, boolean isCancelable) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.fragment_logout_dialog, null);

        builder.setView(view);

        //TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);

        confirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog builderDialog = progressDialog(activity, "Logging out ...", false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.app_name),
                                        activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("profile", null);
                                editor.apply();

                                if (activity != null)
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

        return (AlertDialog) dialog;

    }

    public static AlertDialog progressDialog(Activity activity, String message, boolean isCancelable){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_loading_dialog, null);
        ((TextView)view.findViewById(R.id.textDialogMessage)).setText(message);
        builder.setView(view).setCancelable(isCancelable);

        builder.show().getWindow().getAttributes().windowAnimations = R.style.DialogScale;

        return builder.create();
    }
}
