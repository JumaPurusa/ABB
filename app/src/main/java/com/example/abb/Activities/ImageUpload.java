package com.example.abb.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.abb.MainActivity;
import com.example.abb.R;

public class ImageUpload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Profile Settings");
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        findViewById(R.id.skip_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ImageUpload.this, MainActivity.class));
        finish();
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
