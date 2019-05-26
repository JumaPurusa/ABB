 package com.example.abb.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.example.abb.MainActivity;
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.MySingleton;
import com.example.abb.Utils.SaveSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private Button loginBtn, registerBtn;
    private CoordinatorLayout relativeLayout;

    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(restoreLoginPrefs()){
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

        setContentView(R.layout.activity_login);

        relativeLayout = findViewById(R.id.login_layout);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password_text);
        loginBtn = findViewById(R.id.loginBtn);

        registerBtn = findViewById(R.id.regBtn);
        registerBtn.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Login.this, Registration.class);
                registerIntent.putExtra("login", "login");
                startActivity(registerIntent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        sharedPreferences = getSharedPreferences("introPrefs", MODE_PRIVATE);

        loginBtn.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                final String email = emailEdit.getText().toString();
                final String password = passwordEdit.getText().toString();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Snackbar.make(relativeLayout, "Please provide both your email and password", Snackbar.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    Snackbar.make(relativeLayout, R.string.please_provide_your_email, Snackbar.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Snackbar.make(relativeLayout, R.string.please_enter_password, Snackbar.LENGTH_SHORT).show();
                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches() == false) {
                    Snackbar.make(relativeLayout, "Please enter the valid email", Snackbar.LENGTH_SHORT).show();
                } else {

                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Constants.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    hideKeyboard();
                                    if (response.contains("Please check your email to verify your account")) {

                                        progressDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                        builder.setMessage(response + " Then Login again");

                                        builder.setPositiveButton("Ok", null);
                                        builder.create().show();
                                        mAuth.signOut();

                                    } else if (response.contains("Email or Password is not valid")) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    } else {

                                        //SaveSettings.userProfile(Login.this, response);
                                        //sendToMainActivity();
                                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                                                new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                                        if(task.isSuccessful()){

                                                            sendToMainActivity();
                                                            // save the value if the user to sharedPreferences if the user is successfully logged in
                                                            savePrefsData();
                                                            finish();

                                                        }else{

                                                            Snackbar.make(relativeLayout,
                                                                    task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                                        }

                                                        progressDialog.dismiss();

                                                    }
                                                }
                                        );


                                    }

                                    Log.d("response: ", response);
                                    //Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show( );

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

                                    progressDialog.dismiss();
                                    //mAuth.signOut();
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
        });

        setupFirebaseAuth();
    }

    private boolean restoreLoginPrefs(){

        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("introPrefs", MODE_PRIVATE);

        return preferences.getBoolean("isLoggedIn", false);
    }

    private void savePrefsData(){
        SharedPreferences.Editor prefs = sharedPreferences.edit();
        prefs.putBoolean("isLoggedIn", true);
        prefs.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                // check if the user has logged in

                if(currentUser != null){
                        //sendToMainActivity();
                        //savePrefsData();
                        //finish();
                }



            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    private void sendToMainActivity(){
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        //mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    private void hideKeyboard(){

        View view = getCurrentFocus();
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        emailEdit.setText(""); // clear email text
        passwordEdit.setText(""); // clear password text
        emailEdit.requestFocus();

    }
}
