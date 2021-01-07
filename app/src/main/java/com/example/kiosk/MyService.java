package com.example.kiosk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {

        startInForeground();
        Log.e("EXIT", "MyService Created ");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("EXIT", "MyService Started ");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("EXIT", "ondestroy!");
    }


    private void startInForeground() {

        String channelId = "CHANNEL";

        Intent notificationIntent = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)

                    .setContentTitle("TEST")
                    .setTicker("TICKER")
                    .setSound(null);
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel(channelId, "channel1", NotificationManager.IMPORTANCE_NONE);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.cancelAll();
            startForeground(1, notification);
            startForeground(1, notification);
            stopForeground(true);

        }

    }

}