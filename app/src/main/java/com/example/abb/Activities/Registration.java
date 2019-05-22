package com.example.abb.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private EditText fullnameEdit, usernameEdit, emailEdit, passwordEdit, confirmPassEdit;
    private Button registerBtn;
    private TextView signIn;
    private CoordinatorLayout registerLayout;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        sharedPreferences = getApplicationContext().getSharedPreferences("introPrefs", MODE_PRIVATE);

        registerLayout = findViewById(R.id.register_layout);
        fullnameEdit = findViewById(R.id.full_name);
        usernameEdit = findViewById(R.id.username);
        emailEdit = findViewById(R.id.email_phone);
        passwordEdit = findViewById(R.id.password);
        confirmPassEdit = findViewById(R.id.confirm_password);

        registerBtn = findViewById(R.id.sign_up);

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
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();

                        }
                    }
            );


        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String fullName = fullnameEdit.getText().toString();
                        final String username = usernameEdit.getText().toString();
                        final String email = emailEdit.getText().toString();
                        final String password = passwordEdit.getText().toString();
                        final String conf_password = confirmPassEdit.getText().toString();

                        if(TextUtils.isEmpty(fullName) && TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)
                                && TextUtils.isEmpty(conf_password)){
                            Snackbar.make(registerLayout, R.string.please_fill_all_the_field, Snackbar.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(fullName)){
                            Snackbar.make(registerLayout, R.string.please_fill_all_the_field, Snackbar.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(username)){
                            Snackbar.make(registerLayout, R.string.please_provide_your_username, Snackbar.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(email)){
                            Snackbar.make(registerLayout, R.string.please_provide_your_email, Snackbar.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(password)){
                            Snackbar.make(registerLayout, R.string.please_enter_password, Snackbar.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(conf_password)){
                            Snackbar.make(registerLayout, R.string.please_confirm_password, Snackbar.LENGTH_SHORT).show();
                        }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches() == false){
                            Snackbar.make(registerLayout, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
                        }else if(!conf_password.equals(password)){
                            Snackbar.make(registerLayout, R.string.password_matches, Snackbar.LENGTH_SHORT).show();
                        }else{

                            final ProgressDialog progressDialog = new ProgressDialog(Registration.this);
                            progressDialog.setMessage("Please wait...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>( ) {
                                @Override
                                public void onResponse(String response) {
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(Registration.this);

                                    if(response.contains("Email already exists")){
                                        progressDialog.dismiss();
                                        builder.setTitle(response + ", Please go to login");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener( ) {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent loginIntent = new Intent(Registration.this, Login.class);
                                                startActivity(loginIntent);
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            }
                                        });

                                        builder.setNegativeButton("Cancel", null);
                                        builder.create().show();
                                    }else if(response.contains("Please verify your email")){
                                        progressDialog.setMessage(response);
                                        progressDialog.setCancelable(true);
                                        ProgressDialog progressDialog2 = new ProgressDialog(Registration.this);
                                        progressDialog2.setMessage(response);

                                        // what's next

                                    }

                                    saveRegisterData();
                                    Log.d("response: ", response);
                                    //Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show( );





                                }
                            }, new Response.ErrorListener( ) {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if(error instanceof TimeoutError){
                                        Snackbar.make(registerLayout, "Timeout Error", Snackbar.LENGTH_SHORT).show();
                                    }else if(error instanceof NoConnectionError){
                                        Snackbar.make(registerLayout, "No Connection Error", Snackbar.LENGTH_SHORT).show();
                                    }else if(error instanceof AuthFailureError){
                                        Snackbar.make(registerLayout, "Authentication Failure Error", Snackbar.LENGTH_SHORT).show();
                                    }else if(error instanceof NetworkError){
                                        Snackbar.make(registerLayout, "Network Error", Snackbar.LENGTH_SHORT).show();
                                    }else if(error instanceof ServerError){
                                        Snackbar.make(registerLayout, "Server Error", Snackbar.LENGTH_SHORT).show();
                                    }else if(error instanceof ParseError){
                                        Snackbar.make(registerLayout, "JSON Parse Error", Snackbar.LENGTH_SHORT).show();
                                    }

                                    progressDialog.dismiss();
                                }
                            }){

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
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

                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                        }


                    }
                }
        );

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(getIntent().hasExtra("login")){
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }else{
            finishAffinity();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isRegisterAlready(){
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("introPrefs", MODE_PRIVATE);

        return preferences.getBoolean("isRegisterAlready", false);
    }

    private void saveRegisterData(){

        SharedPreferences.Editor prefs = sharedPreferences.edit();
        prefs.putBoolean("isRegisterAlready", true);
        prefs.apply();
    }

}
