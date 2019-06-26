package com.example.abb.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.abb.Model.Message;
import com.example.abb.Model.User;
import com.example.abb.R;
import com.google.gson.Gson;

public class NotificationDetails extends AppCompatActivity {

    Message message;
    private TextView titleText, dateText, messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Notification");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);


        Intent intent = getIntent();
        if(intent.hasExtra("message")){
            String messageString = intent.getStringExtra("message");

            if(messageString != null);
              message = new Gson().fromJson(messageString, Message.class);
        }

        titleText = findViewById(R.id.titleText);
        titleText.setText(message.getTitle());

        dateText = findViewById(R.id.dateText);
        dateText.setText(message.getSent_date());

        messageText = findViewById(R.id.messageText);
        messageText.setText(message.getMessage());
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
