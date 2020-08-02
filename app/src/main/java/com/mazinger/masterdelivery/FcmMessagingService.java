package com.mazinger.masterdelivery;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/* *
 * Ofertas
 * Created by Alain Nicolás Tello on 07/09/2018 at 05:34pm
 * All rights reserved 2018.
 * Course Specialize in Firebase for Android 2018 with MVP
 * More info: https://www.udemy.com/especialidad-en-firebase-para-android-con-mvp-profesional/
 */
public class FcmMessagingService extends FirebaseMessagingService {
    private static final String DESCUENTO = "descuento";

    public FcmMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null &&
                remoteMessage.getData().size() > 0 && remoteMessage.getNotification() != null){
            sendNotification(remoteMessage);
        } else {
            sendEmptyNotification();
        }
    }

    private void sendEmptyNotification() {
        float desc = 0.15f;

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DESCUENTO, desc);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.rbon)
                .setContentTitle("Title")
                .setContentText("Body")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .addAction(R.drawable.ic_menos, "RECHAZAR", null)
                .addAction(R.drawable.ic_mas,"CONTESTAR", pendingIntent)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.normal_channel_id);
            String channelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 200, 50});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationBuilder.setChannelId(channelId);
        }

        if (notificationManager != null) {
            notificationManager.notify("", 0, notificationBuilder.build());
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String descStr = remoteMessage.getData().get(DESCUENTO);
        float desc = Float.valueOf(descStr != null? descStr : "0");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DESCUENTO, desc);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.src_files_sendyellow)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)

                    .addAction(R.drawable.ic_menos, "RECHAZAR", null)
                    .addAction(R.drawable.ic_mas, "CONTESTAR", pendingIntent)

                    .setContentIntent(pendingIntent);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(desc > .4 ?
                        ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) :
                        ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                // TODO: 14/09/2019 new
                //https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/?hl=es-419#androidmessagepriority
                notificationBuilder.setPriority(Notification.PRIORITY_MAX);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = desc < .10 ? getString(R.string.low_channel_id) :
                        getString(R.string.normal_channel_id);
                String channelName = desc < .10 ? getString(R.string.low_channel_name) :
                        getString(R.string.normal_channel_name);
                NotificationChannel channel = new NotificationChannel(channelId, channelName,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 200, 50});
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }

                notificationBuilder.setChannelId(channelId);
            }

            if (notificationManager != null) {
                notificationManager.notify("", 0, notificationBuilder.build());
            }
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Log.d("newToken", token);
    }
}
