package com.example.abb.Activities;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abb.Model.Donor;
import com.example.abb.R;

public class DonorDetailsActivity extends AppCompatActivity {

    private final static String TAG = DonorDetailsActivity.class.getName();
    private Context mContext = DonorDetailsActivity.this;

    private TextView fullname, phone, email;

    Donor donor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_donor_details);

        // check if the intent has a message (Donor object)
        if(getIntent().hasExtra("Donor")) {
            donor = getIntent().getParcelableExtra("Donor");
        }

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle(donor.getName());
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        fullname = findViewById(R.id.full_name);
        phone = findViewById(R.id.phone_no);
        email = findViewById(R.id.email);

        fullname.setText(getString(R.string.full_name, donor.getName()));
        phone.setText(getString(R.string.phone_number, donor.getPhone_no()));
        email.setText(getString(R.string.email, donor.getEmail()));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
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
}
