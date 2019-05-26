package com.example.abb.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.DrawableContainer;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abb.Activities.Login;
import com.example.abb.Activities.Registration;
import com.example.abb.Dialogs.LoadingDialog;
import com.example.abb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class DatabaseMethods {

    private final static String TAG = DatabaseMethods.class.getName();
    private Context mContext;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String firebase_user_id;

    public DatabaseMethods(Context context){
        this.mContext = context;

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            firebase_user_id = mAuth.getCurrentUser().getUid();
        }

    }

    public void registerNewUserWithEmailAndPassword(final String fullName, final String username,
                                                    final String email, final String password,
                                                    final ProgressDialog progressDialog,
                                                    final CoordinatorLayout layout){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            firebase_user_id = firebaseUser.getUid();
                            addNewUserToDatabase(firebase_user_id, fullName,
                                    StringManipulation.expandedUsername(username), email, password, progressDialog, layout);

                        }else{
                            //showSnackMessage(task.getException().getMessage(), layout);
                            LoadingDialog.messageDialog((Activity) mContext, task.getException().getMessage(), true);
                            progressDialog.dismiss();
                        }
                    }
                }
        );

    }

    public void addNewUserToDatabase(final String firebase_user_id, final String fullName, final String username, final String email, final String password
    , final ProgressDialog progressDialog, final CoordinatorLayout layout){
        Log.d(TAG, "addNewUserToDatabase: Adding New User");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>( ) {
            @Override
            public void onResponse(String response) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(mContext);

                if(response.contains("Email already exists")){
                    progressDialog.dismiss();
                    builder.setTitle(response + ", Please go to login");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener( ) {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loginIntent = new Intent(mContext, Login.class);
                            mContext.startActivity(loginIntent);
                            ((AppCompatActivity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });

                    builder.setNegativeButton("Cancel", null);
                    builder.create().show();
                }else if(response.contains("Please verify your email")){
                    progressDialog.dismiss();
                    //progressDialog.setMessage(response);
                    //progressDialog.setCancelable(true);
                    //ProgressDialog progressDialog2 = new ProgressDialog(mContext);
                   // progressDialog2.setMessage(response);
                    LoadingDialog.messageDialog((Activity) mContext, response, true);
                    // what's next

                }

                //saveRegisterData();
                Log.d("response: ", response);
                //Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show( );





            }
        }, new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError){
                    showSnackMessage("TimeOut Error", layout);
                }else if(error instanceof NoConnectionError){
                    showSnackMessage("No Connection Error", layout);
                }else if(error instanceof AuthFailureError){
                    showSnackMessage("Authentication Failure Error", layout);
                }else if(error instanceof NetworkError){
                    showSnackMessage("Network Error", layout);
                }else if(error instanceof ServerError){
                    showSnackMessage("Server Error", layout);
                }else if(error instanceof ParseError){
                    showSnackMessage("JSON Parse Error", layout);
                }

                progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.FIRE_ID, firebase_user_id);
                params.put(Constants.FULL_NAME, fullName);
                params.put(Constants.USERNAME, username);
                params.put(Constants.EMAIL, email);
                params.put(Constants.PASSWORD, password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "fasthelpapp");
                return headers;
            }
        };

        MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);


    }

    private void showSnackMessage(String message, CoordinatorLayout layout){
        Snackbar.make(layout, message, 2000).show();
    }
}
