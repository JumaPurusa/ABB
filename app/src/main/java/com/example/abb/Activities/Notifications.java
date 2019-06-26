package com.example.abb.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.abb.Adapters.DonorsAdapter;
import com.example.abb.Adapters.NotificationAdapter;
import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Donor;
import com.example.abb.Model.Message;
import com.example.abb.Model.NotificationABB;
import com.example.abb.R;
import com.example.abb.Utils.Actions;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    public static final String DATABASE_NAME = "notification";

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Message> listOfMessages;
    SharedPreferences savedNotifications;
    NotificationABB notificationABB;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        savedNotifications = getSharedPreferences("notifications", MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Notifications");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createNotificationTable();


        listOfMessages = new ArrayList<>();

        Intent intent = getIntent();
        if(intent.hasExtra("id")){


            NotificationABB notificationABB = new NotificationABB();
            notificationABB.setTitle(intent.getStringExtra("title"));
            notificationABB.setDescription(intent.getStringExtra("desc"));
            notificationABB.setDate(intent.getStringExtra("date"));
            notificationABB.setId(intent.getStringExtra("id"));

            addNotification(notificationABB.getId(), notificationABB.getTitle(), notificationABB.getDescription(), notificationABB.getDate());
            //SharedPreferences.Editor prefEditor = savedNotifications.edit();
            //prefEditor.putString(notificationABB.getId(), new Gson().toJson(notificationABB));
            //prefEditor.apply();

            //listOfNotifications.add(notificationABB);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showNotificationFromDatabase();
        //adapter = new NotificationAdapter(this, listOfNotifications);
        //recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(
                new ItemClickListener() {
                    @Override
                    public void itemClick(View view, int position) {

                        Message message = listOfMessages.get(position);
                        String messageString = new Gson().toJson(message);

                        Intent notificationDetails = new Intent(getApplicationContext(), NotificationDetails.class);
                        notificationDetails.putExtra("message", messageString);
                        startActivity(notificationDetails);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );

        adapter.setPopupClickListener(
                new DonorsAdapter.PopupClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void popupClick(View view, int position) {

                        final Message message = listOfMessages.get(position);
                        final String title = message.getTitle() + ", " + message.getSent_date();
                        final String stringMessage = message.getMessage();

                        // creating the instance of PopupMenu
                        PopupMenu popupMenu = new PopupMenu(Notifications.this, view);

                        // Inflating the Popup using xml file
                        popupMenu.getMenuInflater().inflate(R.menu.notification_popup, popupMenu.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popupMenu.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {

                                        switch (menuItem.getItemId()){
                                            case R.id.ic_share:
                                                shareNotification(title, stringMessage);
                                                return true;

                                            case R.id.ic_delete:
                                                deleteNotification(message);
                                                return true;
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

    private void shareNotification(String title, String message){

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Notification via"));

    }

    private void deleteNotification(final Message message){

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setMessage(getString(R.string.delete_notification_confirm, message.getTitle()));

        confirmBuilder.setNegativeButton("Cancel", null);

        confirmBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String sql = "DELETE FROM notification WHERE id = ?";
                mDatabase.execSQL(sql, new String[]{message.getId()});
                reloadNotificationsFromDatabase();

            }
        });

        confirmBuilder.create().show();
    }

    private void reloadNotificationsFromDatabase() {

        listOfMessages.clear();
        Cursor cursorNotifications = mDatabase.rawQuery("SELECT * FROM notification ", null);
        if (cursorNotifications.moveToLast()) {

            do {
                //pushing each record in the employee list
                listOfMessages.add(new Message(
                        cursorNotifications.getString(0),
                        cursorNotifications.getString(1),
                        cursorNotifications.getString(2),
                        cursorNotifications.getString(3)
                ));

            } while (cursorNotifications.moveToPrevious());
        }
        cursorNotifications.close();
        adapter.notifyDataSetChanged();
    }

    private void createNotificationTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS notification (\n" +
                        "    id TEXT PRIMARY KEY,\n" +
                        "    title VARCHAR ,\n" +
                        "    message VARCHAR ,\n" +
                        "    sent_date VARCHAR \n" +
                        ");"
        );
    }

    private void addNotification(String id, String title, String message, String sent_date) {

            String insertSQL = "INSERT INTO notification \n" +
                    "(id, title, message, sent_date)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?);";

            //using the same method execsql for inserting values
            //this time it has two parameters
            //first is the sql string and second is the parameters that is to be binded with the query
            mDatabase.execSQL(insertSQL, new String[]{id, title, message, sent_date});

            Toast.makeText(this, "Notification Added Successfully", Toast.LENGTH_SHORT).show();

    }

    private void showNotificationFromDatabase() {

        //we used rawQuery(sql, selectionargs) for fetching all the notification
        Cursor cursorNotifications = mDatabase.rawQuery("SELECT * FROM notification", null);

        //if the cursor has some data
        if (cursorNotifications.moveToLast()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                listOfMessages.add(new Message(
                        cursorNotifications.getString(0),
                        cursorNotifications.getString(1),
                        cursorNotifications.getString(2),
                        cursorNotifications.getString(3)
                ));
            } while (cursorNotifications.moveToPrevious());
        }
        //closing the cursor
        cursorNotifications.close();

        //creating the adapter object
        adapter = new NotificationAdapter(this, listOfMessages);

        //adding the adapter to listview
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
}
