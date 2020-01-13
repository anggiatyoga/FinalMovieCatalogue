package com.android.anggiat.moviecatalogueapi.database.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.anggiat.moviecatalogueapi.view.activity.FavoriteActivity;
import com.android.anggiat.moviecatalogueapi.database.db.MovieHelper;

import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.AUTHORITY;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.CONTENT_URI;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.TABLE_NAME;

public class MovieFavoriteProvider extends ContentProvider{

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieHelper movieHelper;

    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_ID);

    }


    @Override
    public boolean onCreate() {
//        movieHelper = MovieHelper.getInstance(getContext());
        movieHelper = new MovieHelper(getContext());
        movieHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        movieHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        movieHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteActivity.DataObserver(new android.os.Handler(), getContext()));
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        movieHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteActivity.DataObserver(new android.os.Handler(), getContext()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        movieHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = movieHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteActivity.DataObserver(new android.os.Handler(), getContext()));
        return updated;
    }
}
