package com.example.abb.Utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.abb.R;

public class DialogDisplay {

    public static AlertDialog progressDialogWaiting(Activity activity, String message, boolean isCancelable){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_progress_waiting_dialog, null);

        ((TextView)view.findViewById(R.id.messageText)).setText(message);
        dialogBuilder.setView(view);

        return dialogBuilder.create();
    }
}
