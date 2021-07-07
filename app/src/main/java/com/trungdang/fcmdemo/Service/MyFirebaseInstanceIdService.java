package com.trungdang.fcmdemo.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.trungdang.fcmdemo.MainActivity;
import com.trungdang.fcmdemo.R;

import java.util.Map;

import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    private InputMethodService activity;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("aaaa ",s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData()!=null)
        {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtra("foreground", true);
            i.putExtra("incoming_call", true);

            PowerManager pm = (PowerManager) getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE);

            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl1 = pm.newWakeLock(
                    ACQUIRE_CAUSES_WAKEUP|ON_AFTER_RELEASE|FULL_WAKE_LOCK, "wl1");
            wl1.acquire(10000);


            startActivity(i);

        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String,String> data=remoteMessage.getData();
        String title =data.get("title");
        String content=data.get("content");


        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String NOTIFICATION_CHANNEL_ID="EDMTDev";
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"EDMT Notification",
                    NotificationManager.IMPORTANCE_MAX);

            notificationChannel.setDescription("Trung for app test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }
//        // Create an Intent for the activity you want to start
//        Intent resultIntent = new Intent(this, MainActivity.class);
//// Create the TaskStackBuilder and add the intent, which inflates the back stack
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addNextIntentWithParentStack(resultIntent);
//// Get the PendingIntent containing the entire back stack
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("Trung")
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("info")
                .setSound(defaultSoundUri);



        notificationManager.notify(1,notificationBuilder.build());

    }
}
