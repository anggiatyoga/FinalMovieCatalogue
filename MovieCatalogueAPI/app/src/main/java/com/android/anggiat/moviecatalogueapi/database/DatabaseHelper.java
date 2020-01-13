package com.android.anggiat.moviecatalogueapi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract;
import com.android.anggiat.moviecatalogueapi.database.entity.MovieFavorite;

import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.CATEGORY;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.ID_MOVIE;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.POSTER;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.RATING;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.RELEASE;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.TABLE_NAME;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.TITLE;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "dbMovieFavorite";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE_FAVORITE = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            DatabaseContract.MovieFavoriteColumns.TABLE_NAME,
            DatabaseContract.MovieFavoriteColumns._ID,
            DatabaseContract.MovieFavoriteColumns.ID_MOVIE,
            DatabaseContract.MovieFavoriteColumns.POSTER,
            DatabaseContract.MovieFavoriteColumns.TITLE,
            DatabaseContract.MovieFavoriteColumns.RELEASE,
            DatabaseContract.MovieFavoriteColumns.RATING,
            DatabaseContract.MovieFavoriteColumns.CATEGORY
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MovieFavoriteColumns.TABLE_NAME);
        onCreate(db);
    }

}
