package com.example.abb.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BecomeDonor extends AppCompatActivity {

    private final static String TAG = BecomeDonor.class.getName();
    private Context mContext = BecomeDonor.this;
    public final static String LOCATION_KEY = "location";

    private Spinner spinner;
    private List<String> locations;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_become_donor);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Become Donor");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        spinner = findViewById(R.id.spinner);

        locations = new ArrayList<>();
        locations.add(" ");

        loadData();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(goToDisplayAvailableDonors);

    }

    // handling fab click listener
    private View.OnClickListener goToDisplayAvailableDonors
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(spinner.getSelectedItem() == null || spinner.getSelectedItem() == " ")
                Snackbar.make(findViewById(R.id.request_blood_layout),
                        "Please select location", 2000).show();
            else{
                String location = spinner.getSelectedItem().toString();

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * loading data into the spinner
     */
    private void loadData(){

        Log.d(TAG, "loadData: It get called");
        StringRequest stringRequest = new StringRequest(
                Constants.LOCATION_URL,
                new Response.Listener<String>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(String response) {
                        //fab.setVisibility(View.VISIBLE);
                        // parsing the response using JSON
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("hospital info");

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject hospitalInfo = jsonArray.getJSONObject(i);
                                locations.add(hospitalInfo.getString("location_name"));
                            }


                            adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, locations);
                            spinner.setAdapter(adapter);

                        }catch (JSONException e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //fab.setVisibility(View.INVISIBLE);
                        Snackbar.make(findViewById(R.id.request_blood_layout),
                                error.getMessage(), 2000)
                                .show();

                    }
                }
        ) ;

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
