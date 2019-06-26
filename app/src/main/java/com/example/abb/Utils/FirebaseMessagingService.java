package com.example.abb.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.abb.Activities.Notifications;
import com.example.abb.MainActivity;
import com.example.abb.Model.NotificationABB;
import com.example.abb.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String DATE = "date";
    private static final String ID = "id";

    //List<NotificationABB> notificationList;
    //SharedPreferences savedNotifications = getSharedPreferences("notifications", MODE_PRIVATE);

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //showNotification(remoteMessage.getData().get("message"));

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
           Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }


    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationABB notificationABB = new NotificationABB();
        notificationABB.setTitle(title);
        notificationABB.setDescription(message);

        Intent resultIntent = new Intent(getApplicationContext(), Notifications.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationABB, resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {

        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String date = data.get(DATE);
        String id = data.get(ID);
        //notificationList = new ArrayList<>();

        NotificationABB notificationABB = new NotificationABB();
        notificationABB.setTitle(title);
        notificationABB.setDescription(message);
        notificationABB.setDate(date);
        notificationABB.setId(id);

        //SharedPreferences.Editor prefs = savedNotifications.edit();
        //prefs.putString(notificationABB.getId(), new Gson().toJson(NotificationABB.class));


        Intent resultIntent = new Intent(getApplicationContext(), Notifications.class);
        resultIntent.putExtra("title", notificationABB.getTitle());
        resultIntent.putExtra("desc", notificationABB.getDescription());
        resultIntent.putExtra("date", notificationABB.getDate());
        resultIntent.putExtra("id", notificationABB.getId());
        //resultIntent.putExtra("notification", new Gson().toJson(NotificationABB.class));

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationABB, resultIntent);
        notificationUtils.playNotificationSound();

    }

    /*
    private void showNotification(String message){

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i
        , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Android Blood Bank")
                .setContentText(message)
                .setSmallIcon(R.drawable.abb_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }*/
}
