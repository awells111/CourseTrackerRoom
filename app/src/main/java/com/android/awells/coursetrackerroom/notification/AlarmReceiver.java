package com.android.awells.coursetrackerroom.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.android.awells.coursetrackerroom.MainActivity;
import com.android.awells.coursetrackerroom.R;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ALARM_RECEIVER_INTENT = "ALARM_RECEIVER_INTENT";

    public static final String NOTIFICATION_TITLE_KEY = "NOTIFICATION_TITLE_KEY";

    public static final String NOTIFICATION_CONTENT_KEY = "NOTIFICATION_TITLE_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getStringExtra(ALARM_RECEIVER_INTENT) != null &&
                intent.getStringExtra(ALARM_RECEIVER_INTENT).equals(ALARM_RECEIVER_INTENT)){
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(intent.getStringExtra(NOTIFICATION_TITLE_KEY))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            mainIntent,
                            PendingIntent.FLAG_ONE_SHOT
                    );

            builder.setContentIntent(pendingIntent);
            notificationManager.notify(1, builder.build());
        }

    }
}
