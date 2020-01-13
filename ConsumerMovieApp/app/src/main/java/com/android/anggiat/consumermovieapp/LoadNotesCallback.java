package com.android.anggiat.consumermovieapp;

import android.database.Cursor;

public interface LoadNotesCallback {
    void preExecute();
    void postExecute(Cursor movieFavorite);
}
