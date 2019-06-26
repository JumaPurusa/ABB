package com.example.abb.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.DialogDisplay;
import com.example.abb.Utils.MySingleton;
import com.example.abb.Utils.NetworkUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private static final String TAG = Registration.class.getName();

    private EditText firstNameEdit, lastNameEdit, usernameEdit, emailEdit, passwordEdit, confirmPassEdit;
    private Button registerBtn;
    private CoordinatorLayout registerLayout;
    private LinearLayout contentLayout;
    private AlertDialog progressDialog;

    private SharedPreferences sharedPreferences;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Sign up");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        registerLayout = findViewById(R.id.register_layout);
        firstNameEdit = findViewById(R.id.firstNameEditText);
        lastNameEdit = findViewById(R.id.lastNameEditText);
        usernameEdit = findViewById(R.id.usernameEditText);
        emailEdit = findViewById(R.id.email_phone);
        passwordEdit = findViewById(R.id.passwordEditText);
        confirmPassEdit = findViewById(R.id.confirm_passwordEditText);

        contentLayout = findViewById(R.id.contentLayout);



        registerBtn = findViewById(R.id.sign_up);

        progressDialog = DialogDisplay.progressDialogWaiting(
                Registration.this,
                "Please Wait...",
                false
        );


        final Intent intent = getIntent();


        findViewById(R.id.sign_in).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(intent.hasExtra("login")){
                                finish();
                                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            }else{
                                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                                startActivity(loginIntent);
                                finish();
                            }
                            //startActivity(new Intent(getApplicationContext(), Login.class));
                            //finish();


                        }
                    }
            );


        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // check if the form is valid
                        if(isFormValid())
                            // check if is online
                            if(NetworkUtils.isConnected(Registration.this))
                                onStartRegister();
                            else
                                Snackbar.make(registerLayout,
                                        "No Connection",
                                        2000).show();


                        }


                    }
        );

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        //requestQueue = MySingleton.getInstance(getApplicationContext())
                //.getRequestQueue();

    }


    private void onStartRegister(){

        // check if the dialog is showing
        if(!progressDialog.isShowing())
            progressDialog.show();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        registerTask(firstNameEdit.getText().toString(),
                                lastNameEdit.getText().toString(),
                                usernameEdit.getText().toString(),
                                emailEdit.getText().toString(),
                                passwordEdit.getText().toString(), token);

                    }
                });



        progressDialog.findViewById(R.id.cancelText).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestQueue = MySingleton.getInstance(getApplicationContext()).
                                getRequestQueue();
                        if(requestQueue != null) {
                            requestQueue.cancelAll(TAG);

                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                }
        );

    }


    private boolean isFormValid( ){
        if(TextUtils.isEmpty(firstNameEdit.getText().toString()) && TextUtils.isEmpty(lastNameEdit.getText().toString())
                && TextUtils.isEmpty(usernameEdit.getText().toString())
                && TextUtils.isEmpty(emailEdit.getText().toString()) && TextUtils.isEmpty(passwordEdit.getText().toString())
                && TextUtils.isEmpty(confirmPassEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.please_fill_all_the_field, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(firstNameEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.please_provide_your_firstname, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(lastNameEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.please_provide_your_lastname, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(lastNameEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.please_provide_your_username, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(emailEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.please_provide_your_email, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(passwordEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.please_enter_password, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(confirmPassEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.please_confirm_password, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches() == false){
            Snackbar.make(registerLayout, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(!confirmPassEdit.getText().toString().equals(passwordEdit.getText().toString())){
            Snackbar.make(registerLayout, R.string.password_matches, Snackbar.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }

    private void onErrorOccurred(){

        firstNameEdit.setText(""); // clear first name
        lastNameEdit.setText(""); // clear last name
        usernameEdit.setText(""); // clear username
        emailEdit.setText(""); // clear email
        passwordEdit.setText(""); // clear password
        confirmPassEdit.setText(""); // clear confirm password

        showSnackMessage("Error has occurred. Please try again"
        ,registerLayout);
    }

    private void onAuthenticationFailure(String message){

        if(message.contains("Error in registering, another account is using the email"))
            emailEdit.setText(""); // clear email
        else if(message.contains("Error in registering. Probably the username already exists"))
            usernameEdit.setText(""); // clear username
        else if(message.contains("Error in registering. data was not inserted")){
            firstNameEdit.setText(""); // clear first name
            lastNameEdit.setText(""); // clear last name
            usernameEdit.setText(""); // clear username
            emailEdit.setText(""); // clear email
            passwordEdit.setText(""); // clear password
            confirmPassEdit.setText(""); // clear confirm password
        }else if(message.contains("Error in registering. Email Address not valid")){
            emailEdit.setText(""); // clear email
        }

        showSnackMessage(message,
                registerLayout);
    }

    private void onSuccessRegister(){


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", emailEdit.getText().toString());
        editor.putString("password", passwordEdit.getText().toString());
        editor.apply();

        startActivity(new Intent(Registration.this, NextToUploadActivity.class));
        finish();

    }


    private void registerTask(final String firstName, final String lastName, final String username, final String email,
                           final String password, final String token){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>( ) {
            @Override
            public void onResponse(String response) {

                // cancel the dialog
                if(progressDialog.isShowing())
                    progressDialog.dismiss();

                int success = 0;
                String message = "";

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    success = jsonResponse.getInt("success");

                    if(jsonResponse.getString("msg") != null)
                        message = jsonResponse.getString("msg");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(response == null || response == "<br>" || response == "<!DOCTYPE"){
                    onErrorOccurred();
                }else if(success == 0){

                    onAuthenticationFailure(message);

                }else {

                    if (success == 1) {

                        onSuccessRegister();
                    }

                }

            }
        }, new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError){
                    showSnackMessage("TimeOut Error", registerLayout);
                }else if(error instanceof NoConnectionError){
                    showSnackMessage("No Connection Error", registerLayout);
                }else if(error instanceof AuthFailureError){
                    showSnackMessage("Authentication Failure Error", registerLayout);
                }else if(error instanceof NetworkError){
                    showSnackMessage("Network Error", registerLayout);
                }else if(error instanceof ServerError){
                    showSnackMessage("Server Error", registerLayout);
                }else if(error instanceof ParseError){
                    showSnackMessage("JSON Parse Error", registerLayout);
                }

                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.FIRST_NAME, firstName);
                params.put(Constants.LAST_NAME, lastName);
                params.put(Constants.USERNAME, username);
                params.put(Constants.EMAIL, email);
                params.put(Constants.PASSWORD, password);
                params.put("token", token);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "fasthelpapp");
                return headers;
            }
        };

        stringRequest.setTag(TAG);
        MySingleton.getInstance(Registration.this).addToRequestQueue(stringRequest);

    }

    private void showSnackMessage(String message, CoordinatorLayout layout){
        Snackbar.make(layout, message, 2000).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        requestQueue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();

        if(requestQueue != null)
            requestQueue.cancelAll(TAG);

        if(getIntent().hasExtra("login")){
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }else{
            finishAffinity();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(progressDialog.isShowing())
            progressDialog.dismiss();

        requestQueue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();

        if(requestQueue != null)
            requestQueue.cancelAll(TAG);
    }


}
