package com.android.anggiat.consumermovieapp.database;

import android.database.Cursor;

import com.android.anggiat.consumermovieapp.entity.MovieFavorite;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.CATEGORY;
import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.ID_MOVIE;
import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.POSTER;
import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.RATING;
import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.RELEASE;
import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.TITLE;

public class MappingHelper {

    public static ArrayList<MovieFavorite> mapCursorToArrayList(Cursor movieFavoriteCursor) {
        ArrayList<MovieFavorite> movieFavoriteArrayList = new ArrayList<>();

        while (movieFavoriteCursor.moveToNext()) {
            int id = movieFavoriteCursor.getInt(movieFavoriteCursor.getColumnIndexOrThrow(_ID));
            String idMovie = movieFavoriteCursor.getString(movieFavoriteCursor.getColumnIndexOrThrow(ID_MOVIE));
            String poster = movieFavoriteCursor.getString(movieFavoriteCursor.getColumnIndexOrThrow(POSTER));
            String title = movieFavoriteCursor.getString(movieFavoriteCursor.getColumnIndexOrThrow(TITLE));
            String release = movieFavoriteCursor.getString(movieFavoriteCursor.getColumnIndexOrThrow(RELEASE));
            String rating = movieFavoriteCursor.getString(movieFavoriteCursor.getColumnIndexOrThrow(RATING));
            String category = movieFavoriteCursor.getString(movieFavoriteCursor.getColumnIndexOrThrow(CATEGORY));
            movieFavoriteArrayList.add(new MovieFavorite(id, idMovie, poster, title, release, rating, category));
        }
        return movieFavoriteArrayList;

    }

}
