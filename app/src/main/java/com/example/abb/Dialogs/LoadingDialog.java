package com.example.abb.Dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.abb.R;


public class LoadingDialog {

     public static AlertDialog progressDialog(Activity activity, String message, boolean isCancelable){

         AlertDialog.Builder builder = new AlertDialog.Builder(activity);
         View view = activity.getLayoutInflater().inflate(R.layout.fragment_loading_dialog, null);
         ((TextView)view.findViewById(R.id.textDialogMessage)).setText(message);
         builder.setView(view).setCancelable(isCancelable);

         builder.show().getWindow().getAttributes().windowAnimations = R.style.DialogScale;

         return builder.create();
     }

     public static AlertDialog messageDialog(final Activity activity, String message, boolean isCancelable){

         AlertDialog.Builder messageBuilder = new AlertDialog.Builder(activity);
         messageBuilder.setMessage(message);
         messageBuilder.setCancelable(isCancelable);

         messageBuilder.setPositiveButton("OK", null);

         Dialog dialog = messageBuilder.create();
         dialog.getWindow().getAttributes().windowAnimations = R.style.DialogScale;
         dialog.show();
         return (AlertDialog) dialog;
     }
}
