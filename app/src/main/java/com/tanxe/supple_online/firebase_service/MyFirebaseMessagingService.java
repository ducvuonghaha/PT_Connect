package com.tanxe.supple_online.firebase_service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.screen.HomeActivity;
import com.tanxe.supple_online.screen.ProfileActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("title", remoteMessage.getNotification().getTitle() + "");
        Log.e("title1", remoteMessage.getData().get("title") + "");

        if (remoteMessage.getNotification().getTitle().equals("PT Connect phản hồi")){
            Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("title", remoteMessage.getNotification().getTitle());
//        getApplicationContext().startActivity(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            intent.putExtra("title", remoteMessage.getNotification().getTitle());
            intent.putExtra("body", remoteMessage.getNotification().getTitle());
            intent.setAction("com.CUSTOM");
            sendBroadcast(intent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",NotificationManager.IMPORTANCE_MAX);

                // Configure the notification channel.
                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setContentIntent(pendingIntent);



            notificationManager
                    .notify(1410, notificationBuilder.build());
        }else {
            Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra("title", remoteMessage.getNotification().getTitle());
//        getApplicationContext().startActivity(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            intent.putExtra("title", remoteMessage.getNotification().getTitle());
            intent.putExtra("body", remoteMessage.getNotification().getTitle());
            intent.setAction("com.CUSTOM");
            sendBroadcast(intent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",NotificationManager.IMPORTANCE_MAX);

                // Configure the notification channel.
                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setContentIntent(pendingIntent);



            notificationManager
                    .notify(1410, notificationBuilder.build());
        }
//        try {
//
//            count++;
//            json = new JSONObject(remoteMessage.getData());
//            Log.d(TAG, "JSONObject: "+json.toString());
//            JSONObject messageData = new JSONObject(json.getString("message"));
//            BaseMessage baseMessage = CometChatHelper.processMessage(new JSONObject(remoteMessage.getData().get("message")));
//            if (baseMessage instanceof Call){
//                call = (Call)baseMessage;
//                isCall=true;
//            }
//            showNotifcation(baseMessage);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }





    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("Key", s);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("My Token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",s);
        editor.apply();
    }


}
