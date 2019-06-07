package com.example.abb.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.abb.Adapters.HospitalAdapter;
import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Hospital;
import com.example.abb.R;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.MySingleton;
import com.example.abb.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BecomeDonor extends AppCompatActivity {

    private final static String TAG = BecomeDonor.class.getName();
    private Context mContext = BecomeDonor.this;

    private EditText searchQueryEditText;
    private CoordinatorLayout coordinatorLayout;

    private RecyclerView recyclerView;
    private HospitalAdapter adapter;
    private List<Hospital> hospitals;

    private TextView textView;
    //private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_become_donor);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Become Donor");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        textView = findViewById(R.id.textView);

        searchQueryEditText = ((TextInputLayout)findViewById(R.id.query_text_input_layout))
                .getEditText();
        searchQueryEditText.addTextChangedListener(textWatcher);

        hospitals = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new HospitalAdapter(hospitals);
        adapter.setItemClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);

        //loadData("");

        //fab = findViewById(R.id.fab);
        //fab.setOnClickListener(goToDisplayAvailableDonors);
    }

    private TextWatcher textWatcher =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                       loadData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                        //loadData(s.toString());
                }
            };

    private ItemClickListener itemClickListener = new
            ItemClickListener() {
                @Override
                public void itemClick(View view, int position) {
                    Hospital hospital = hospitals.get(position);
                    Intent hospitalIntent = new Intent(BecomeDonor.this, DonorRegisterHospital.class);
                    hospitalIntent.putExtra("Hospital", (Parcelable) hospital);
                    startActivity(hospitalIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }
            };


    // handling fab click listener
    private View.OnClickListener goToDisplayAvailableDonors
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            loadData(searchQueryEditText.getText().toString());
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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * loading data into the spinner
     */
    private void loadData(final String searchQuery){

        Log.d(TAG, "loadData: It get called :" + searchQuery);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.HOSPITAL_SEARCH,
                new Response.Listener<String>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(String response) {
                        //fab.setVisibility(View.VISIBLE);
                        // parsing the response using JSON
                        //textView.setText(response);
                        //Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        hospitals.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                            for(int i=0; i<jsonArray.length(); i++){

                                Hospital hospital = new Hospital();
                                JSONObject hospitalInfo = jsonArray.getJSONObject(i);
                                hospital.setHospitalName(hospitalInfo.getString("hospital_name"));
                                hospital.setHospitalLocation(hospitalInfo.getString("location_name"));
                                hospital.setHospitalContacts(hospitalInfo.getString("hospital_contacts"));
                                hospitals.add(hospital);
                            }


                            //HospitalAdapter hospitalAdapter = new HospitalAdapter(hospitals, itemClickListener);
                            adapter.notifyDataSetChanged();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //fab.setVisibility(View.INVISIBLE);

                        if (error instanceof TimeoutError) {
                            showSnackBarMessage("Timeout Error", coordinatorLayout);
                        } else if (error instanceof NoConnectionError) {
                            showSnackBarMessage("No Connection Error", coordinatorLayout);
                        } else if (error instanceof AuthFailureError) {
                            showSnackBarMessage("Authentication Failure Error", coordinatorLayout);
                        } else if (error instanceof NetworkError) {
                            showSnackBarMessage("Network Error", coordinatorLayout);
                        } else if (error instanceof ServerError) {
                            showSnackBarMessage("Server Error",coordinatorLayout);
                        } else if (error instanceof ParseError) {
                            showSnackBarMessage("JSON Parse Error", coordinatorLayout);
                        }


                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.SEARCH_QUERY, searchQuery);
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

    private void showSnackBarMessage(String message, CoordinatorLayout layout){
        Snackbar.make(coordinatorLayout,
                message, 5000)
                .show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!NetworkUtils.isConnected(BecomeDonor.this))
            showSnackBarMessage("There is no internet connection", coordinatorLayout);
    }
}
