package com.android.anggiat.moviecatalogueapi.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.view.activity.MainActivity;

import java.util.Calendar;

public class DailyReminderReceiver extends BroadcastReceiver{

    public static final String EXTRA_MESSAGE_PREF = "message";
    public static final String EXTRA_TYPE_PREF = "type";
    public final static int NOTIFICATION_DAILY_ID = 501;

    private static final CharSequence CHANNEL_NAME = "dicoding channel";
    private static final String CHANNEL_ID = "channel_01";


    public DailyReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showReminderNotification(context, context.getString(R.string.check_some_movie_today), intent.getStringExtra(EXTRA_MESSAGE_PREF), NOTIFICATION_DAILY_ID);
    }

    private void showReminderNotification(Context context, String title, String desc, int id) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(NOTIFICATION_DAILY_ID, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uriRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, desc)
                .setSmallIcon(R.drawable.ic_movie_grey_24dp)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(uriRingtone);

        // Untuk android oreo ke atas
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(desc, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//            builder.setChannelId(desc);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
        // Untuk android oreo ke atas

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }


        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());
        }
    }

    public void setReminderNotification(Context context, String type, String time, String message) {

        cancelReminderNotification(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE_PREF, message);
        intent.putExtra(EXTRA_TYPE_PREF, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_DAILY_ID, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(context, R.string.turn_on_daily_app_reminder, Toast.LENGTH_SHORT).show();
    }

    public void cancelReminderNotification(Context context) {

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_DAILY_ID, intent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, R.string.turn_off_daily_app_reminder, Toast.LENGTH_SHORT).show();

    }


}
