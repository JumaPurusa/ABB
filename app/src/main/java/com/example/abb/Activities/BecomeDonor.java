package com.example.abb.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.abb.R;

public class BecomeDonor extends AppCompatActivity {

    private  ImageView arrowBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_donor);

        findViewById(R.id.ic_arrow_back).findViewById(R.id.ic_arrow_back)
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }
                );
    }
}
