package com.android.anggiat.consumermovieapp.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    static final String AUTHORITY = "com.android.anggiat.moviecatalogueapi";
    private static final String SCHEME = "content";

    public DatabaseContract() {}

    public static final class MovieFavoriteColumns implements BaseColumns {
        public static final String TABLE_NAME = "movieFavorite";
        public static String ID_MOVIE = "idMovie";
        public static String POSTER = "poster";
        public static String TITLE = "title";
        public static String RELEASE = "release";
        public static String RATING = "rating";
        public static String CATEGORY = "category";


        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName){
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

}

