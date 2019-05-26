package com.example.abb.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abb.Activities.Login;
import com.example.abb.R;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutDialog extends DialogFragment {


    private final  static String TAG = LogoutDialog.class.getName();
    private Context mContext;

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LogoutDialog(){

    }

    @SuppressLint("ValidFragment")
    public LogoutDialog(Context context){
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout_dialog, container, false);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogScale;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setupFirebaseAuth();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.cancel)
                .setOnClickListener(onTextClickListener);

        view.findViewById(R.id.confirm)
                .setOnClickListener(onTextClickListener);

    }

    // handler the cancel or confirm click listener
    private View.OnClickListener onTextClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.cancel:
                    getDialog().dismiss();
                    break;

                case R.id.confirm:
                    getDialog().dismiss();
                    showLoadingDialog();
                    break;
            }
        }
    };

    private void showLoadingDialog(){

        LoadingDialog.progressDialog(getActivity(), "Logging out ...", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mAuth.signOut();
                SharedPreferences preferences = mContext.getSharedPreferences("introPrefs", mContext.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Activity activity = (Activity)mContext;

                if(activity != null)
                activity.startActivity(new Intent(mContext, Login.class));
                //activity.finish();

            }
        }, 3000);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                }
            }
        };
    }

}
