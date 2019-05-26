package com.example.abb.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abb.Adapters.SpinnerViewAdapter;
import com.example.abb.MainActivity;
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestBlood extends AppCompatActivity {

    private final static String TAG = RequestBlood.class.getName();
    private Context mContext = RequestBlood.this;
    public final static String LOCATION_KEY = "location";

    private Spinner spinner;
    private List<String> locations;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton fab;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_request_blood);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Request Blood");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        spinner = findViewById(R.id.spinner);

        locations = new ArrayList<>();
        locations.add(" ");


        //jsonRequest();

        loadData();

        fab = findViewById(R.id.fab);
        //fab.setVisibility(View.INVISIBLE);
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
                startActivity(new Intent(RequestBlood.this, DonorsActivity.class)
                .putExtra(LOCATION_KEY, location));
                //Toast.makeText(mContext, location, Toast.LENGTH_SHORT).show();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
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
                // exit activity
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
                Constants.REQUEST_BLOOD_URL,
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
