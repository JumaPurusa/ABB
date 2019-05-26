package com.example.abb.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.abb.Utils.DatabaseMethods;
import com.example.abb.Utils.MySingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private EditText fullnameEdit, usernameEdit, emailEdit, passwordEdit, confirmPassEdit;
    private Button registerBtn;
    private TextView signIn;
    private CoordinatorLayout registerLayout;

    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
                            //startActivity(new Intent(getApplicationContext(), Login.class));
                            //finish();


                        }
                    }
            );


        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        formProcessing(fullnameEdit.getText().toString(),
                                usernameEdit.getText().toString(),
                                emailEdit.getText().toString(),
                                passwordEdit.getText().toString(),
                                confirmPassEdit.getText().toString());




                        }


                    }
        );

        setupFirebaseAuth();

    }


    private void formProcessing(String fullName, String username, String email, String password, String confirmPass){

        if(TextUtils.isEmpty(fullName) && TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)
                && TextUtils.isEmpty(confirmPass)){
            Snackbar.make(registerLayout, R.string.please_fill_all_the_field, Snackbar.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(fullName)){
            Snackbar.make(registerLayout, R.string.please_fill_all_the_field, Snackbar.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(username)){
            Snackbar.make(registerLayout, R.string.please_provide_your_username, Snackbar.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(email)){
            Snackbar.make(registerLayout, R.string.please_provide_your_email, Snackbar.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(password)){
            Snackbar.make(registerLayout, R.string.please_enter_password, Snackbar.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(confirmPass)){
            Snackbar.make(registerLayout, R.string.please_confirm_password, Snackbar.LENGTH_SHORT).show();

        }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches() == false){
            Snackbar.make(registerLayout, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();

        }else if(!confirmPass.equals(password)){
            Snackbar.make(registerLayout, R.string.password_matches, Snackbar.LENGTH_SHORT).show();

        }else {

            final ProgressDialog progressDialog = new ProgressDialog(Registration.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            DatabaseMethods databaseMethods = new DatabaseMethods(Registration.this);
            databaseMethods.registerNewUserWithEmailAndPassword(fullName, username, email, password,
                    progressDialog, registerLayout);

        }

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

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if(mAuthListener != null)
            //mAuth.removeAuthStateListener(mAuthListener);
    }

    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();

        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){

                }
                    //mAuth.signOut();
            }
        };
        */
    }
}
