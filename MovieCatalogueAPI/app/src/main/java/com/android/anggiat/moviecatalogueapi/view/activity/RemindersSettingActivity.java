package com.android.anggiat.moviecatalogueapi.view.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.notification.DailyReminderReceiver;
import com.android.anggiat.moviecatalogueapi.notification.MovieReleaseReminderReceiver;
import com.android.anggiat.moviecatalogueapi.notification.NotificationReminderPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class RemindersSettingActivity extends AppCompatActivity {

    public static final String KEY_HEADER_UPCOMING_REMINDER = "upcomingReminder";
    public static final String KEY_HEADER_DAILY_REMINDER = "dailyReminder";
    public static final String KEY_FIELD_UPCOMING_REMINDER = "checkedUpcoming";
    public static final String KEY_FIELD_DAILY_REMINDER = "checkedDaily";
    public static final String TYPE_REMINDER_PREF = "reminderAlarm";
    public static final String TYPE_REMINDER_RECIEVE = "reminderAlarmRelease";

    @BindView(R.id.switch_daily_reminder)
    Switch switchDailyReminder;
    @BindView(R.id.switch_release_reminder)
    Switch switchReleaseReminder;
    public DailyReminderReceiver dailyReminderReceiver;
    public MovieReleaseReminderReceiver movieReleaseReminderReceiver;
    public NotificationReminderPreference notificationReminderPreference;
    public SharedPreferences sReleaseReminder, sDailyReminder;
    public SharedPreferences.Editor editorReleaseReminder, editorDailyReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_setting);
        ButterKnife.bind(this);

        dailyReminderReceiver = new DailyReminderReceiver();
        movieReleaseReminderReceiver = new MovieReleaseReminderReceiver();
        notificationReminderPreference = new NotificationReminderPreference(this);
        setPreference();

     }

    private void movieReleaseReminderOn() {
        String time = "08:00";
        String message = getString(R.string.release_today_check_and_watch_it);
        notificationReminderPreference.setNotificationReminderMovieTime(time);
        notificationReminderPreference.setNotificationReminderMovieMessage(message);
        movieReleaseReminderReceiver.setReminderNotification(RemindersSettingActivity.this, TYPE_REMINDER_PREF, time, message);
    }

    private void movieReleaseReminderOff() {
        movieReleaseReminderReceiver.cancelReminderNotification(RemindersSettingActivity.this);
    }

    private void dailyReminderOn() {
        String time = "07:00";
        String message = getString(R.string.check_some_movie_today);
        notificationReminderPreference.setNotificationReminderAppTime(time);
        notificationReminderPreference.setNotificationReminderAppMessage(message);
        dailyReminderReceiver.setReminderNotification(RemindersSettingActivity.this, TYPE_REMINDER_RECIEVE, time, message);
    }

    private void dailyReminderOff() {
        dailyReminderReceiver.cancelReminderNotification(RemindersSettingActivity.this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @OnCheckedChanged(R.id.switch_daily_reminder)
    public void setSwitchDailyReminder(boolean isChecked) {
        editorDailyReminder = sDailyReminder.edit();
        if (isChecked) {
            editorDailyReminder.putBoolean(KEY_FIELD_DAILY_REMINDER, true);
            editorDailyReminder.commit();
            dailyReminderOn();
        } else {
            editorDailyReminder.putBoolean(KEY_FIELD_DAILY_REMINDER, false);
            editorDailyReminder.commit();
            dailyReminderOff();
        }
    }

    @OnCheckedChanged(R.id.switch_release_reminder)
    public void setSwitchReleaseReminder(boolean isChecked) {
        editorReleaseReminder = sReleaseReminder.edit();
        if (isChecked) {
            editorReleaseReminder.putBoolean(KEY_FIELD_UPCOMING_REMINDER, true);
            editorReleaseReminder.commit();
            movieReleaseReminderOn();
        } else {
            editorReleaseReminder.putBoolean(KEY_FIELD_UPCOMING_REMINDER, false);
            editorReleaseReminder.commit();
            movieReleaseReminderOff();
        }
    }

    private void setPreference() {
        sReleaseReminder = getSharedPreferences(KEY_HEADER_UPCOMING_REMINDER, MODE_PRIVATE);
        sDailyReminder = getSharedPreferences(KEY_HEADER_DAILY_REMINDER, MODE_PRIVATE);
        boolean checkUpcomingReminder = sReleaseReminder.getBoolean(KEY_FIELD_UPCOMING_REMINDER, false);
        switchReleaseReminder.setChecked(checkUpcomingReminder);
        boolean checkDailyReminder = sDailyReminder.getBoolean(KEY_FIELD_DAILY_REMINDER, false);
        switchDailyReminder.setChecked(checkDailyReminder);
    }


}
