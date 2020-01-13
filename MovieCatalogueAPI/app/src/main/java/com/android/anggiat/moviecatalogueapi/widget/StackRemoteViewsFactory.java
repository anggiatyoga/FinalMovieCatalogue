package com.android.anggiat.moviecatalogueapi.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.database.entity.MovieFavorite;
import com.android.anggiat.moviecatalogueapi.model.pojo.Movie;
import com.android.anggiat.moviecatalogueapi.model.pojo.TvShow;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.CONTENT_URI;
import static com.android.anggiat.moviecatalogueapi.widget.MovieAppWidget.EXTRA_ITEM;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    int mMovieWidgetId;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.mContext = applicationContext;
        this.mMovieWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long token = Binder.clearCallingIdentity();
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(token);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    //"https://image.tmdb.org/t/p/w154"
    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Movie movie = getMoviePosition(position);
        String moviePoster = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        String title = movie.getTitle();
        String rating = movie.getVoteAverage();

        try {
            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(moviePoster)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
            remoteViews.setTextViewText(R.id.text_movie_title_widget, title);
            remoteViews.setTextViewText(R.id.text_movie_category_widget, rating);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(EXTRA_ITEM, position);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.imageView, fillIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private Movie getMoviePosition(int position) {
        if (cursor.moveToPosition(position)) {
            return new Movie(cursor);
        } else {
            throw new IllegalStateException("The position is invalid");
        }
    }

}
