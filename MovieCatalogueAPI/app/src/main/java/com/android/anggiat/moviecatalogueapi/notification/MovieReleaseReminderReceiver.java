package com.android.anggiat.moviecatalogueapi.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.model.api.ApiClient;
import com.android.anggiat.moviecatalogueapi.model.api.ApiService;
import com.android.anggiat.moviecatalogueapi.model.pojo.Movie;
import com.android.anggiat.moviecatalogueapi.model.pojo.MovieResult;
import com.android.anggiat.moviecatalogueapi.view.activity.DetailMovieActivity;
import com.android.anggiat.moviecatalogueapi.view.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.anggiat.moviecatalogueapi.BuildConfig.API_KEY;

public class MovieReleaseReminderReceiver extends BroadcastReceiver{

    public static final String TAG = MovieReleaseReminderReceiver.class.getSimpleName();
    public static final String EXTRA_MESSAGE_RECIEVE = "messageRelease";
    public static final String EXTRA_TYPE_RECIEVE = "typeRelease";
    public final static int NOTIFICATION_RELEASE_ID = 502;
    public ArrayList<Movie> movieArrayList = new ArrayList<>();
    public List<Movie> movieList = new ArrayList<>();

    private static final CharSequence CHANNEL_NAME = "dicoding channel";
    private static final String CHANNEL_ID = "channel_01";

    private static final int MAX_NOTIFICATION = 2;
    private final static String GROUP_KEY_MOVIES = "group_key_movies";
    private int notifId = 0;

    public MovieReleaseReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getReleaseMovieDate(context);
    }

    private void getReleaseMovieDate(final Context context) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = simpleDateFormat.format(date);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<MovieResult> movieResultCall = apiService.getReleaseMovie(API_KEY, todayDate, todayDate);
        movieResultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                Log.d(TAG, "onResponse response: " + response);
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().getResults() != null) {
                            movieList = response.body().getResults();
                            for (int i=0; i<movieList.size(); i++) {
                                notifId = i++;
                            }
                            String title = movieList.get(notifId).getTitle();
                            String message = movieList.get(notifId).getOverview();
                            String idMovie = String.valueOf(movieList.get(notifId).getId());
                            showReminderNotification(context, title, message, notifId, idMovie);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.d(TAG, "Something wrong parsing JSON");
            }
        });

    }

    private void showReminderNotification(Context context, String title, String message, int notifId, String idMovie) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder;
        Uri uriRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (notifId < MAX_NOTIFICATION) {
            builder = new NotificationCompat.Builder(context)
                    .setContentTitle(String.valueOf(notifId))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_movie_grey_24dp)
                    .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(uriRingtone)
                    .setGroup(GROUP_KEY_MOVIES)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(movieList.get(notifId).getTitle())
                    .addLine(movieList.get(notifId - 1).getTitle())
                    .addLine(movieList.get(notifId - 2).getTitle())
                    .setBigContentTitle(notifId + " movie release")
                    .setSummaryText("New Movie Release");
            builder = new NotificationCompat.Builder(context)
                    .setContentTitle(notifId + " new movie release")
                    .setContentText("Release Movie")
                    .setSmallIcon(R.drawable.ic_movie_grey_24dp)
                    .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(uriRingtone)
                    .setGroup(GROUP_KEY_MOVIES)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }


        // Untuk android oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        // Untuk android oreo ke atas


        if (notificationManager != null) {
            notificationManager.notify(notifId, builder.build());
        }
    }

    // COBA STACK NOTIF

    public void setReminderNotification(Context context, String type, String time, String message) {
        cancelReminderNotification(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReleaseReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE_RECIEVE, message);
        intent.putExtra(EXTRA_TYPE_RECIEVE, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_RELEASE_ID, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, R.string.turn_on_movie_release_reminder, Toast.LENGTH_SHORT).show();

    }


    public void cancelReminderNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReleaseReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_RELEASE_ID, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, R.string.turn_off_movie_release_reminder, Toast.LENGTH_SHORT).show();
    }

}
