package com.example.abb.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abb.Adapters.DonorsAdapter;
import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.MainActivity;
import com.example.abb.Model.Donor;
import com.example.abb.R;
import com.example.abb.Utils.Actions;
import com.example.abb.Utils.Constants;
import com.example.abb.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DonorsActivity extends AppCompatActivity {

    private final static String TAG = DonorsActivity.class.getName();

    private ProgressBar progressBar;
    private CoordinatorLayout donorsLayout;
    private RecyclerView recyclerView;
    private DonorsAdapter donorsAdapter;
    private List<Donor> donors;
    private TextView noDonorsText;

    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_donors);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        if(getIntent().hasExtra(RequestBlood.LOCATION_KEY))
            location = getIntent().getStringExtra(RequestBlood.LOCATION_KEY);
        else
            location = "";

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle(location);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        donorsLayout = findViewById(R.id.donors_layout);
        progressBar = findViewById(R.id.progressBar);
        noDonorsText = findViewById(R.id.no_donors);

        donors = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        donorsAdapter = new DonorsAdapter(this, donors);
        recyclerView.setAdapter(donorsAdapter);

        loadData(location);

        donorsAdapter.setItemClickListener(
                new ItemClickListener() {
                    @Override
                    public void itemClick(View view, int position) {

                        Donor donor = donors.get(position);
                        Intent detailIntent = new Intent(DonorsActivity.this, DonorDetailsActivity.class);
                        detailIntent.putExtra("Donor", (Parcelable) donor);
                        startActivity(detailIntent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }
                }
        );

        donorsAdapter.setPopupClickListener(
                new DonorsAdapter.PopupClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void popupClick(View view, int position) {

                        final Donor donor = donors.get(position);

                        // creating the instance of PopupMenu
                        PopupMenu popupMenu = new PopupMenu(DonorsActivity.this, view);

                        // Inflating the Popup using xml file
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popupMenu.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {

                                        switch (menuItem.getItemId()){
                                            case R.id.action_call:
                                                Actions.onCallingDonor(
                                                       DonorsActivity.this,
                                                       donor,
                                                        donorsLayout
                                                );
                                                return true;

                                            case R.id.action_sms:
                                                Actions.onMessagingDonor(
                                                        DonorsActivity.this,
                                                        donor,
                                                        donorsLayout
                                                );
                                                return true;

                                            case R.id.action_email:
                                                Actions.onEmailingDonor(
                                                        DonorsActivity.this,
                                                        donor,
                                                        donorsLayout
                                                );

                                        }
                                        return false;
                                    }
                                }
                        );

                        popupMenu.setGravity(Gravity.RIGHT);

                        try {
                            Field mFieldPopup= popupMenu.getClass().getDeclaredField("mPopup");
                            mFieldPopup.setAccessible(true);
                            MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupMenu);
                            //mPopup.setForceShowIcon(true);
                        } catch (Exception e) {
                        }

                        popupMenu.show(); // showing PopupMenu
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData(final String location){

        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "loadData: It get called");
        StringRequest stringRequest = new StringRequest(
                createURL(location),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        progressBar.setVisibility(View.INVISIBLE);
                        // parsing the response using JSON

                        if(response.contains("There are no donors in " + location +" yet.")){
                            noDonorsText.setVisibility(View.VISIBLE);
                            noDonorsText.setText(response);
                            //Toast.makeText(DonorsActivity.this, response, Toast.LENGTH_SHORT).show();
                        }

                        //noDonorsText.setVisibility(View.INVISIBLE);
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("donors info");

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject donorObject = jsonArray.getJSONObject(i);
                                Donor donor = new Donor();
                                donor.setLocation_name(donorObject.getString("location_name"));
                                donor.setName(
                                        donorObject.getString("fname") + " " + donorObject.getString("lname")
                                );
                                donor.setEmail(donorObject.getString("email"));
                                donor.setPhone_no(donorObject.getString("phone_no"));
                                donor.setBlood_group(donorObject.getString("blood_group"));

                                donors.add(donor);
                            }

                            Log.d(TAG, "onResponse: List: " + donors);
                            donorsAdapter.notifyDataSetChanged();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(donorsLayout, error.getMessage(), 200)
                                .show();
                    }
                }
        ) ;

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String createURL(String newLocation){

        String baseURL = Constants.REQUEST_DONORS;

        try {
            String urlString = baseURL + "location=" + URLEncoder.encode(newLocation, "UTF-8");
            return  urlString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
