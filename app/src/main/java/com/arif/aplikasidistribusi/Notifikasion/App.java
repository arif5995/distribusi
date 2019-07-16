package com.arif.aplikasidistribusi.Notifikasion;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Created by Angga Kristiono on 18/06/2019.
 */

public class App extends Application {
    public static final  String CHANNEL_1 = "channel1";

    @Override
    public void onCreate() {
        super.onCreate();
        
        createNotification();
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1,
                    "chanel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is chnel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
