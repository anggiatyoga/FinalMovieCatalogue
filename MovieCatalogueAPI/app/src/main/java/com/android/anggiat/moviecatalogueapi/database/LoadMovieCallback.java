package com.android.anggiat.moviecatalogueapi.database;

import android.database.Cursor;

import com.android.anggiat.moviecatalogueapi.database.entity.MovieFavorite;

import java.util.ArrayList;

public interface LoadMovieCallback {

    void preExecute();
    void postExecute(Cursor movieFavorites);

}
