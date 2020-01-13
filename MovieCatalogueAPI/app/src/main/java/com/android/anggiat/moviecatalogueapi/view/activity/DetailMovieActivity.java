package com.android.anggiat.moviecatalogueapi.view.activity;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anggiat.moviecatalogueapi.BuildConfig;
import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.database.DatabaseHelper;
import com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract;
import com.android.anggiat.moviecatalogueapi.database.db.MovieHelper;
import com.android.anggiat.moviecatalogueapi.database.entity.MovieFavorite;
import com.android.anggiat.moviecatalogueapi.model.api.ApiClient;
import com.android.anggiat.moviecatalogueapi.model.api.ApiService;
import com.android.anggiat.moviecatalogueapi.model.pojo.Movie;
import com.android.anggiat.moviecatalogueapi.model.pojo.MovieDetails;
import com.android.anggiat.moviecatalogueapi.model.pojo.MovieGenres;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.CATEGORY;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.CONTENT_URI;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.ID_MOVIE;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.POSTER;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.RATING;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.RELEASE;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.TABLE_NAME;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.TITLE;

public class DetailMovieActivity extends AppCompatActivity{

    public static final String EXTRA_MOVIE = "extra_movie";

    public static final String EXTRA_FAVORITE_MOVIE = "extra_favorite_movie";
    public static final String EXTRA_FAVORITE_POSITION = "extra_favorite_position";

    SharedPreferences preferences;
    private boolean isFavorite = false;

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private final String TAG = DetailMovieActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private static final String API_KEY = BuildConfig.API_KEY;
    private ArrayList<MovieGenres> movieGenresList = new ArrayList<>();
    private MovieDetails detailsMovie = new MovieDetails();

    private MovieFavorite movieFavorite;

    @BindView(R.id.image_movie_poster)
    ImageView imageMoviePoster;
    @BindView(R.id.text_movie_name)
    TextView textMovieName;
    @BindView(R.id.text_movie_genre)
    TextView textMovieGenre;
    @BindView(R.id.text_movie_runtime)
    TextView textMovieRuntime;
    @BindView(R.id.text_movie_rating)
    TextView textMovieRating;
    @BindView(R.id.image_movie_backdrop)
    ImageView imageMovieBackdrop;
    @BindView(R.id.text_movie_language)
    TextView textMovieLanguage;
    @BindView(R.id.text_movie_overview)
    TextView textMovieOverview;
    @BindView(R.id.text_movie_released)
    TextView textMovieReleased;
    @BindView(R.id.text_movie_revenue)
    TextView textMovieRevenue;
    Button btnSaveFavorite;

    String idMovie, title, poster, release, rating, category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnSaveFavorite = findViewById(R.id.btn_save_favorite);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        movieFavorite = getIntent().getParcelableExtra(EXTRA_FAVORITE_MOVIE);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movie != null) {
            isFavorite = false;
            idMovie = String.valueOf(movie.getId());
            title = movie.getTitle();
            poster = movie.getPosterPath();
            release = movie.getReleaseDate();
            rating = movie.getVoteAverage();
        } else {
            idMovie = String.valueOf(movieFavorite.getIdMovie());
            title = movieFavorite.getTitle();
            poster = movieFavorite.getPoster();
            release = movieFavorite.getRelease();
            rating = movieFavorite.getRating();
            rating = movieFavorite.getCategory();
        }


        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movieFavorite = new MovieFavorite(cursor);
                cursor.close();
            }
        }

        if (savedInstanceState != null) {
            detailsMovie = savedInstanceState.getParcelable("details_movie");
            movieGenresList = savedInstanceState.getParcelableArrayList("movie_genre_list");
            if (movieGenresList != null) {
                textMovieGenre = findViewById(R.id.text_movie_genre);
                textMovieGenre.setText(movieGenresList.get(0).getName());
            }if ( movieGenresList == null ) {
                Toast.makeText(this, "movieGenreList Null", Toast.LENGTH_SHORT).show();
            }
        } else {
            moviesData();
        }


        //coba coba //
        btnSaveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String idMovie = String.valueOf(detailsMovie.getId());
            String poster = detailsMovie.getPosterPath();
            String title = detailsMovie.getTitle();
            String release = detailsMovie.getReleaseDate();
            String rating = detailsMovie.getVoteAverage();
            String category = "Movie";

            MovieFavorite movieFavorite = new MovieFavorite();
            movieFavorite.setIdMovie(idMovie);
            movieFavorite.setPoster(poster);
            movieFavorite.setTitle(title);
            movieFavorite.setRelease(release);
            movieFavorite.setRating(rating);
            movieFavorite.setCategory(category);

                if (isFavorite) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(EXTRA_FAVORITE_MOVIE + movieFavorite.getIdMovie(), false);
                    editor.commit();
                    isFavorite = false;
                    Toast.makeText(DetailMovieActivity.this, R.string.favorite_deleted, Toast.LENGTH_SHORT).show();
                    deleteFavorite();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(EXTRA_FAVORITE_MOVIE+ movieFavorite.getIdMovie(), true);
                    editor.commit();
                    isFavorite = true;
                    Toast.makeText(DetailMovieActivity.this, R.string.favorite_saved, Toast.LENGTH_SHORT).show();
                    saveFavorite();
                }
                updateFavorite();
            }
        });

        isFavorite = preferences.getBoolean(EXTRA_FAVORITE_MOVIE + idMovie, false);
        updateFavorite();

        //coba coba //

    }

    private void moviesData() {
        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        movieFavorite = getIntent().getParcelableExtra(EXTRA_FAVORITE_MOVIE);

        progressDialog = new ProgressDialog(DetailMovieActivity.this);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();

        Log.d(TAG, "Load detailsMovie Movie");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        final Call<MovieDetails> detailCall;

        if (movie != null) {
            detailCall = apiService.getDetailMovie(movie.getId(), API_KEY);
        } else {
            detailCall = apiService.getDetailMovie(Integer.valueOf(movieFavorite.getIdMovie()), API_KEY);
        }

        detailCall.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    detailsMovie = response.body();
                    movieGenresList = response.body().getMovieGenres();

                    Log.d(TAG, "Get detailsMovie Success :) ");

                    textMovieName.setText(detailsMovie.getTitle());
                    textMovieRuntime.setText(detailsMovie.getRuntime());
                    textMovieRating.setText(detailsMovie.getVoteAverage());
                    textMovieLanguage.setText(detailsMovie.getOriginalLanguage());
                    textMovieOverview.setText(detailsMovie.getOverview());
                    textMovieReleased.setText(detailsMovie.getReleaseDate());
                    textMovieRevenue.setText(detailsMovie.getRevenue());
                    textMovieGenre.setText(movieGenresList.get(0).getName());

                    Glide.with(DetailMovieActivity.this)
                            .load("https://image.tmdb.org/t/p/w500" + detailsMovie.getBackdropPath())
                            .into(imageMovieBackdrop);
                    Glide.with(DetailMovieActivity.this)
                            .load("https://image.tmdb.org/t/p/w154" + detailsMovie.getPosterPath())
                            .into(imageMoviePoster);
                } else {
                    Log.d(TAG, "Get detailsMovie into else :) ");
                    Toast.makeText(DetailMovieActivity.this, R.string.get_detail_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d(TAG, "Get detailsMovie Failed :)");
                Toast.makeText(DetailMovieActivity.this, R.string.failure, Toast.LENGTH_SHORT).show();
            }
        });

        ButterKnife.bind(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("details_movie", detailsMovie);
        outState.putParcelableArrayList("movie_genre_list", movieGenresList);
        super.onSaveInstanceState(outState);
    }


    private void updateFavorite() {
        if (isFavorite) {
            if (movieFavorite == null){
                btnSaveFavorite.setText(R.string.already_saved);
                btnSaveFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DetailMovieActivity.this, R.string.favorite_already_exists, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                btnSaveFavorite.setText(R.string.delete);
            }
        } else {
            btnSaveFavorite.setText(R.string.save);
        }
    }

    private void deleteFavorite() {

        try {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_FAVORITE_MOVIE, movieFavorite);
            getContentResolver().delete(getIntent().getData(), null, null);
            startActivityForResult(intent, DetailMovieActivity.RESULT_DELETE);
            Toast.makeText(this, R.string.deleted_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.d(TAG, "a");
        }

    }

    private void saveFavorite() {
        String idMovie = String.valueOf(detailsMovie.getId());
        String poster = detailsMovie.getPosterPath();
        String title = detailsMovie.getTitle();
        String release = detailsMovie.getReleaseDate();
        String rating = detailsMovie.getVoteAverage();
        String category = "Movie";

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_MOVIE, idMovie);
        contentValues.put(TITLE, title);
        contentValues.put(POSTER, poster);
        contentValues.put(RELEASE, release);
        contentValues.put(RATING, rating);
        contentValues.put(CATEGORY, category);

        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);
        if (ContentUris.parseId(uri) > 0) {
            Toast.makeText(this, "Save data successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Save data failed", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

}
