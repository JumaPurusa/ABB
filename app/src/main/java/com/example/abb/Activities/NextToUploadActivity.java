package com.example.abb.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.example.abb.Model.User;
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.JSONParser;
import com.example.abb.Utils.MySingleton;
import com.example.abb.Utils.NetworkUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class NextToUploadActivity extends AppCompatActivity {

    private static final String TAG = NextToUploadActivity.class.getName();
    private Context mContext = NextToUploadActivity.this;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_to_upload);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Finishing sign up");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        findViewById(R.id.nextButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(NetworkUtils.isConnected(NextToUploadActivity.this))
                            onGoToNextScreen();
                        else
                            //Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                            Snackbar.make(
                                    findViewById(R.id.coordinatorLayout),
                                    "Connection Failed",
                                    2000
                            );

                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onGoToNextScreen(){

        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        onNextTask(email, password);
    }

    private void onSuccessNext(String response){

        Log.d(TAG, "onSuccessLogin: response: " + response);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("profile", response);
        prefsEditor.apply();

        startActivity(new Intent(mContext, ImageUpload.class));
        finish();
    }

    private void onNextTask(final String email, final String password){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //hideKeyboard();
                        if (response.contains("Please check your email to verify your account")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage(response);

                            builder.setPositiveButton("Ok", null);
                            builder.create().show();


                        } else if (response.contains("Email or Password is not valid")) {
                            //Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            Snackbar.make(
                                    findViewById(R.id.coordinatorLayout),
                                    response,
                                    5000
                            );
                        } else {

                            User user = JSONParser.parseUser(response);

                            if(user != null){
                                //user.setEmail(emailEdit.getText().toString());
                                onSuccessNext(new Gson().toJson(user));
                            }



                        }

                        Log.d("response: ", response);
                        //Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show( );

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                        if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(), "Timeout Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "No Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "JSON Parse Error", Toast.LENGTH_SHORT).show();
                        }

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

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }


}
