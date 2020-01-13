package com.android.anggiat.moviecatalogueapi.view.activity;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import com.android.anggiat.moviecatalogueapi.database.entity.MovieFavorite;
import com.android.anggiat.moviecatalogueapi.model.api.ApiClient;
import com.android.anggiat.moviecatalogueapi.model.api.ApiService;
import com.android.anggiat.moviecatalogueapi.model.pojo.TvShow;
import com.android.anggiat.moviecatalogueapi.model.pojo.TvShowDetails;
import com.android.anggiat.moviecatalogueapi.model.pojo.TvShowGenres;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

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
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.TITLE;

public class DetailTvActivity extends AppCompatActivity{
    public static final String EXTRA_TV = "extra_tv";

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
    private ArrayList<TvShowGenres> tvGenresList = new ArrayList<>();
    private TvShowDetails detailsTv = new TvShowDetails();
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
    Button btnRemoveFavorite;

    String idMovie, title, poster, release, rating, category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnSaveFavorite = findViewById(R.id.btn_save_favorite);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        movieFavorite = getIntent().getParcelableExtra(EXTRA_FAVORITE_MOVIE);
        TvShow tvShow = getIntent().getParcelableExtra(EXTRA_TV);

        if (tvShow != null) {
            idMovie = String.valueOf(tvShow.getId());
            title = tvShow.getName();
            poster = tvShow.getPosterPath();
            release = tvShow.getFirstAirDate();
            rating = tvShow.getVoteAverage();
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

        if (savedInstanceState != null){
            detailsTv = savedInstanceState.getParcelable("details_tv");
            tvGenresList = savedInstanceState.getParcelableArrayList("tv_genres_list");
            if (tvGenresList != null) {
                textMovieGenre = findViewById(R.id.text_movie_genre);
                textMovieGenre.setText(tvGenresList.get(0).getName());
            }if ( tvGenresList == null ) {
                Toast.makeText(this, "tvGenreList Null", Toast.LENGTH_SHORT).show();
            }
        } else {
            tvShowData();
        }

        btnSaveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMovie = String.valueOf(detailsTv.getId());
                String poster = detailsTv.getPosterPath();
                String title = detailsTv.getName();
                String release = detailsTv.getFirstAirDate();
                String rating = String.valueOf(detailsTv.getVoteAverage());
                String category = "Tv Show";

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
                    Toast.makeText(DetailTvActivity.this, R.string.favorite_deleted, Toast.LENGTH_SHORT).show();
                    deleteFavorite();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(EXTRA_FAVORITE_MOVIE+ movieFavorite.getIdMovie(), true);
                    editor.commit();
                    isFavorite = true;
                    Toast.makeText(DetailTvActivity.this, R.string.favorite_saved, Toast.LENGTH_SHORT).show();
                    saveFavorite();
                }
                updateFavorite();

            }
        });

        isFavorite = preferences.getBoolean(EXTRA_FAVORITE_MOVIE + idMovie, false);
        updateFavorite();

    }

    private void tvShowData() {
        TvShow tvShow = getIntent().getParcelableExtra(EXTRA_TV);

        progressDialog = new ProgressDialog(DetailTvActivity.this);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();

        Log.d(TAG, "Load detailsMovie Tv Show");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        final Call<TvShowDetails> detailTvCall;

        if (tvShow != null) {
            detailTvCall = apiService.getDetailTv(tvShow.getId(), API_KEY);
        } else {
            detailTvCall = apiService.getDetailTv(Integer.valueOf(movieFavorite.getIdMovie()), API_KEY);
        }

        detailTvCall.enqueue(new Callback<TvShowDetails>() {
            @Override
            public void onResponse(Call<TvShowDetails> call, Response<TvShowDetails> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    detailsTv = response.body();
                    tvGenresList = response.body().getGenres();

                    Log.d(TAG, "Get detailsMovie Success :) ");

                    textMovieName.setText(detailsTv.getName());
                    String getNumberEpisodeTv = String.valueOf(detailsTv.getNumberOfEpisodes());
                    textMovieRuntime.setText(getNumberEpisodeTv);//number episodes
                    String getVoteAverageTv = String.valueOf(detailsTv.getVoteAverage());
                    textMovieRating.setText(getVoteAverageTv);
                    textMovieLanguage.setText(detailsTv.getOriginalLanguage());
                    textMovieOverview.setText(detailsTv.getOverview());
                        textMovieReleased.setText(detailsTv.getFirstAirDate());
                    String getPopularity = String.valueOf(detailsTv.getPopularity());
                    textMovieRevenue.setText(getPopularity);//popularity
                    textMovieGenre.setText(tvGenresList.get(0).getName());

                    Glide.with(DetailTvActivity.this)
                            .load("https://image.tmdb.org/t/p/w500" + detailsTv.getBackdropPath())
                            .into(imageMovieBackdrop);
                    Glide.with(DetailTvActivity.this)
                            .load("https://image.tmdb.org/t/p/w154" + detailsTv.getPosterPath())
                            .into(imageMoviePoster);
                } else {
                    Log.d(TAG, "Get detailsMovie into else :) ");
                    Toast.makeText(DetailTvActivity.this, R.string.get_detail_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TvShowDetails> call, Throwable t) {
                Log.d(TAG, "Get detailsMovie Failed :)");
                Toast.makeText(DetailTvActivity.this, R.string.failure, Toast.LENGTH_SHORT).show();
            }
        });
        ButterKnife.bind(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("details_tv", detailsTv);
        outState.putParcelableArrayList("tv_genres_list", tvGenresList);
        super.onSaveInstanceState(outState);
    }

    private void updateFavorite() {
        if (isFavorite) {
            if (movieFavorite == null){
                btnSaveFavorite.setText(R.string.already_saved);
                btnSaveFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DetailTvActivity.this, R.string.favorite_already_exists, Toast.LENGTH_SHORT).show();
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
            startActivityForResult(intent, DetailTvActivity.RESULT_DELETE);
            Toast.makeText(this, R.string.deleted_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.d(TAG, "a");
        }

    }

    private void saveFavorite() {
        String idMovie = String.valueOf(detailsTv.getId());
        String poster = detailsTv.getPosterPath();
        String title = detailsTv.getName();
        String release = detailsTv.getFirstAirDate();
        String rating = String.valueOf(detailsTv.getVoteAverage());
        String category = "Tv Show";

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

