package com.android.anggiat.moviecatalogueapi.notification;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationReminderPreference {

    private final static String KEY_REMINDER_MESSAGE_MOVIE = "reminderMessageRelease";
    private final static String KEY_REMINDER_MESSAGE_APP = "reminderMessageDaily";
    private final static String KEY_REMINDER_DAILY = "DailyReminder";
    private final static String PREF_NAME = "reminderPreferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public NotificationReminderPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setNotificationReminderMovieTime(String time) {
        editor.putString(KEY_REMINDER_DAILY, time);
        editor.commit();
    }

    public void setNotificationReminderMovieMessage(String message) {
        editor.putString(KEY_REMINDER_MESSAGE_MOVIE, message);
    }

    public void setNotificationReminderAppTime(String time) {
        editor.putString(KEY_REMINDER_DAILY, time);
        editor.commit();
    }

    public void setNotificationReminderAppMessage(String message) {
        editor.putString(KEY_REMINDER_MESSAGE_APP, message);
    }

}
