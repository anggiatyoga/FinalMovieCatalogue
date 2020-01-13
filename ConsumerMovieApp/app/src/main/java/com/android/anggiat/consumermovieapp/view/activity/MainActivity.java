package com.android.anggiat.consumermovieapp.view.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.anggiat.consumermovieapp.LoadNotesCallback;
import com.android.anggiat.consumermovieapp.R;
import com.android.anggiat.consumermovieapp.adapter.ConsumerAdapter;
import com.android.anggiat.consumermovieapp.entity.MovieFavorite;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.CONTENT_URI;
import static com.android.anggiat.consumermovieapp.database.MappingHelper.mapCursorToArrayList;
import static com.android.anggiat.consumermovieapp.view.activity.DetailMovieActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements LoadNotesCallback {

    private static final String EXTRA_STATE = "EXTRA_STATE";
    private ConsumerAdapter consumerAdapter;
    private MainActivity.DataObserver myObserver;
    RecyclerView rvMovies;
    ProgressBar progressBar;

    //BUG FIXED//
    //BUG FIXED//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_load_movie);

        getSupportActionBar().setTitle("Consumer App");

        rvMovies = findViewById(R.id.rv_movie);
        consumerAdapter = new ConsumerAdapter(this);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setHasFixedSize(true);

        rvMovies.setAdapter(consumerAdapter);
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        new getData(this, this).execute();

        if (savedInstanceState == null) {
            new getData(this, this).execute();
        } else {
            ArrayList<MovieFavorite> movieFavoriteArrayList = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movieFavoriteArrayList != null) {
                consumerAdapter.setMovieFavoriteArrayList(movieFavoriteArrayList);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, consumerAdapter.getMovieFavoriteArrayList());
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
    public void postExecute(Cursor movieFavorite) {
        progressBar.setVisibility(View.GONE);
        ArrayList<MovieFavorite> movieFavoriteArrayList = mapCursorToArrayList(movieFavorite);
        if (movieFavoriteArrayList.size() > 0 ) {
            consumerAdapter.setMovieFavoriteArrayList(movieFavoriteArrayList);
        } else {
            consumerAdapter.setMovieFavoriteArrayList(new ArrayList<MovieFavorite>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

     private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;

        private getData(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             weakCallback.get().preExecute();
         }

         @Override
         protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
         }

         @Override
         protected void onPostExecute(Cursor data) {
             super.onPostExecute(data);
             weakCallback.get().postExecute(data);
         }
     }

     static class DataObserver extends ContentObserver {
        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

         @Override
         public void onChange(boolean selfChange) {
             super.onChange(selfChange);
             new getData(context, (LoadNotesCallback) context).execute();
         }
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_UPDATE) {
                if (resultCode == DetailMovieActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(DetailMovieActivity.EXTRA_FAVORITE_POSITION, 0);
                    consumerAdapter.removeItem(position);
                    showSnackbarMessage("Satu item berhasil Dihapus");
                }
            }
        }

        if (data != null) {
            if (requestCode == DetailMovieActivity.REQUEST_ADD) {
                if (resultCode == DetailMovieActivity.RESULT_ADD) {
                    MovieFavorite movieFavorite = data.getParcelableExtra(DetailMovieActivity.EXTRA_FAVORITE_MOVIE);
                    consumerAdapter.addItem(movieFavorite);
                    rvMovies.smoothScrollToPosition(consumerAdapter.getItemCount() - 1);
                    showSnackbarMessage("Satu item berhasil Ditambahkan");
                }
            }
            else if (requestCode == REQUEST_UPDATE) {
                if (resultCode == DetailMovieActivity.RESULT_UPDATE) {
                    MovieFavorite movieFavorite = data.getParcelableExtra(DetailMovieActivity.EXTRA_FAVORITE_MOVIE);
                    int position = data.getIntExtra(DetailMovieActivity.EXTRA_FAVORITE_POSITION, 0);
                    consumerAdapter.updateItem(position, movieFavorite);
                    rvMovies.smoothScrollToPosition(position);
                    showSnackbarMessage("Satu item berhasil Diubah");
                }
                else if (resultCode == DetailMovieActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(DetailMovieActivity.EXTRA_FAVORITE_POSITION, 0);
                    consumerAdapter.removeItem(position);
                    showSnackbarMessage("Satu item berhasil Dihapus");
                }
            }
        }

    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvMovies, message, Snackbar.LENGTH_LONG).show();
    }
}
