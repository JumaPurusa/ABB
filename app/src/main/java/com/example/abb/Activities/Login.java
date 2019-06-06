package com.example.abb.Activities;

import android.app.Dialog;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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
import com.example.abb.MainActivity;
import com.example.abb.Model.User;
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.DialogDisplay;
import com.example.abb.Utils.JSONParser;
import com.example.abb.Utils.MySingleton;
import com.example.abb.Utils.NetworkUtils;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getName();

    private EditText emailEdit, passwordEdit;
    private CoordinatorLayout relativeLayout;
    private SharedPreferences sharedPreferences;

    private AlertDialog progressDialog;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        if(restoreLoginPrefs()){
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            finish();
        }*/

        setContentView(R.layout.activity_login);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Login");
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        relativeLayout = findViewById(R.id.login_layout);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password_text);



        findViewById(R.id.regBtn).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(Login.this, Registration.class);
                registerIntent.putExtra("login", "login");
                startActivity(registerIntent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                onClearEditText();
            }
        });

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        progressDialog = DialogDisplay.progressDialogWaiting(
                Login.this,
                "Please Wait...",
                false
        );


        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                hideKeyboard();

                if(isFormValid())
                    // check if there in network connection
                    if(NetworkUtils.isConnected(Login.this))
                        onStartLogin();
                    else
                        Snackbar.make(relativeLayout,
                                "No Connection",
                                2000).show();
            }
        });

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

    }

    private void onStartLogin(){

        if(!progressDialog.isShowing())
            progressDialog.show();


        loginTask(emailEdit.getText().toString(),
                passwordEdit.getText().toString());

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

    private boolean isFormValid(){

        if (TextUtils.isEmpty(emailEdit.getText().toString()) && TextUtils.isEmpty(passwordEdit.getText().toString())) {
            Snackbar.make(relativeLayout, "Please provide both your email and password", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(emailEdit.getText().toString())) {
            Snackbar.make(relativeLayout, R.string.please_provide_your_email, Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
            Snackbar.make(relativeLayout, R.string.please_enter_password, Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches() == false) {
            Snackbar.make(relativeLayout, "Please enter the valid email", Snackbar.LENGTH_SHORT).show();
            return false;
        }else
            return true;


    }

    private void onErrorOccurred(){

        onClearEditText();

        Snackbar.make(relativeLayout,
                "Error Occured. Please try again",
                5000).show();
    }

    private void onSuccessLogin(String response){

        Log.d(TAG, "onSuccessLogin: response: " + response);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("profile", response);
        prefsEditor.apply();

        sendToMainActivity();

    }

    private void loginTask(final String email, final String password){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        if (response.contains("Please check your email to verify your account")) {

                            passwordEdit.setText("");

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setMessage(response + " Then Login again");
                            builder.setCancelable(true);

                            builder.setPositiveButton("Ok", null);
                            Dialog dialog = builder.create();
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogScale;
                            dialog.show();

                        } else if (response.contains("Email or Password is not valid")) {

                            onClearEditText();
                            Snackbar.make(relativeLayout,
                                    response,
                                    5000).show();

                        }else if(response == null || response.contains("<br>") || response.contains("<!DOCTYPE")){

                            onErrorOccurred();

                        }else{

                            User user = JSONParser.parseUser(response);

                            if(user != null){
                                user.setEmail(emailEdit.getText().toString());
                                onSuccessLogin(new Gson().toJson(user));
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        hideKeyboard();

                        if (error instanceof TimeoutError) {
                            Toast.makeText(Login.this, "Timeout Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(Login.this, "No Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Login.this, "Authentication Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Login.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(Login.this, "JSON Parse Error", Toast.LENGTH_SHORT).show();
                        }

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }

        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.EMAIL, email);
                params.put(Constants.PASSWORD, password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "abb");
                return headers;
            }
        };

        stringRequest.setTag(TAG);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();

        requestQueue = MySingleton.getInstance(getApplicationContext())
                .getRequestQueue();

        if(requestQueue != null){
            requestQueue.cancelAll(TAG);

            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
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

        requestQueue = MySingleton.getInstance(getApplicationContext())
                .getRequestQueue();

        if(requestQueue != null)
            requestQueue.cancelAll(TAG);
    }

    private void sendToMainActivity(){
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        //mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void hideKeyboard(){

        View view = getCurrentFocus();
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void onClearEditText(){
        emailEdit.setText("");
        passwordEdit.setText("");
        emailEdit.requestFocus();
    }
}
