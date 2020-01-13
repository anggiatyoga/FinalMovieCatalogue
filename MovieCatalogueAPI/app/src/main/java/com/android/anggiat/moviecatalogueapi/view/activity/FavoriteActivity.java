package com.android.anggiat.moviecatalogueapi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.view.adapter.FavoriteAdapter;
import com.android.anggiat.moviecatalogueapi.database.LoadMovieCallback;
import com.android.anggiat.moviecatalogueapi.database.entity.MovieFavorite;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.CONTENT_URI;
import static com.android.anggiat.moviecatalogueapi.database.helper.MappingHelper.mapCursorToArrayList;
import static com.android.anggiat.moviecatalogueapi.view.activity.DetailMovieActivity.REQUEST_UPDATE;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener, LoadMovieCallback {

    private RecyclerView rvFavoriteMovie;
    private ProgressBar progressBar;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private FavoriteAdapter favoriteAdapter;

    private static HandlerThread handlerThread;
    private DataObserver myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.favorite_movie_title);
        }

        rvFavoriteMovie = findViewById(R.id.rv_favorite_movie);
        rvFavoriteMovie.setLayoutManager(new LinearLayoutManager(this));
        rvFavoriteMovie.setHasFixedSize(true);

        progressBar = findViewById(R.id.progressbar);

        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        favoriteAdapter = new FavoriteAdapter(this);
        rvFavoriteMovie.setAdapter(favoriteAdapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(this, this).execute();
        } else {
            ArrayList<MovieFavorite> movieFavoriteArrayList = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movieFavoriteArrayList != null) {
                favoriteAdapter.setMovieFavoriteArrayList(movieFavoriteArrayList);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rv_favorite_movie) {
            Toast.makeText(this, "intent to detail item favorite", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.getMovieFavoriteArrayList());
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(Cursor movieFavorites) {
        progressBar.setVisibility(View.INVISIBLE);
        ArrayList<MovieFavorite> movieFavoriteArrayList = mapCursorToArrayList(movieFavorites);
        if (movieFavoriteArrayList.size() > 0) {
            favoriteAdapter.setMovieFavoriteArrayList(movieFavoriteArrayList);
        } else {
            favoriteAdapter.setMovieFavoriteArrayList(new ArrayList<MovieFavorite>());
            showSnackbarMessage(getString(R.string.there_is_no_data_at_the_moment));
        }
    }


    public static class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakMovieCallback;

        public LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakMovieCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakMovieCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movieFavorites) {
            super.onPostExecute(movieFavorites);
            weakMovieCallback.get().postExecute(movieFavorites);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsync(context, (LoadMovieCallback) context).execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_UPDATE) {
                if (resultCode == DetailMovieActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(DetailMovieActivity.EXTRA_FAVORITE_POSITION, 0);
                    favoriteAdapter.removeItem(position);
                    showSnackbarMessage(getString(R.string.one_item_successfully_deleted));
                }
            }
        }

        if (data != null) {
            if (requestCode == DetailMovieActivity.REQUEST_ADD) {
                if (resultCode == DetailMovieActivity.RESULT_ADD) {
                    MovieFavorite movieFavorite = data.getParcelableExtra(DetailMovieActivity.EXTRA_FAVORITE_MOVIE);
                    favoriteAdapter.addItem(movieFavorite);
                    rvFavoriteMovie.smoothScrollToPosition(favoriteAdapter.getItemCount() - 1);
                    showSnackbarMessage(getString(R.string.one_item_successfully_added));
                }
            }
            else if (requestCode == REQUEST_UPDATE) {
                if (resultCode == DetailMovieActivity.RESULT_UPDATE) {
                    MovieFavorite movieFavorite = data.getParcelableExtra(DetailMovieActivity.EXTRA_FAVORITE_MOVIE);
                    int position = data.getIntExtra(DetailMovieActivity.EXTRA_FAVORITE_POSITION, 0);
                    favoriteAdapter.updateItem(position, movieFavorite);
                    rvFavoriteMovie.smoothScrollToPosition(position);
                    showSnackbarMessage(getString(R.string.one_item_successfully_changed));
                }
                else if (resultCode == DetailMovieActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(DetailMovieActivity.EXTRA_FAVORITE_POSITION, 0);
                    favoriteAdapter.removeItem(position);
                    showSnackbarMessage(getString(R.string.one_item_successfully_deleted));
                }
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvFavoriteMovie, message, Snackbar.LENGTH_LONG).show();
    }

}

