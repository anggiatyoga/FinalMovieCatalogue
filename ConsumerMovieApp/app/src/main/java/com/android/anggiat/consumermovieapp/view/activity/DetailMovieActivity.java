package com.android.anggiat.consumermovieapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anggiat.consumermovieapp.BuildConfig;
import com.android.anggiat.consumermovieapp.R;
import com.android.anggiat.consumermovieapp.entity.MovieFavorite;
import com.android.anggiat.consumermovieapp.model.api.ApiClient;
import com.android.anggiat.consumermovieapp.model.api.ApiService;
import com.android.anggiat.consumermovieapp.model.pojo.movie.MovieDetails;
import com.android.anggiat.consumermovieapp.model.pojo.movie.MovieGenres;
import com.android.anggiat.consumermovieapp.model.pojo.tv.TvShow;
import com.android.anggiat.consumermovieapp.model.pojo.tv.TvShowDetails;
import com.android.anggiat.consumermovieapp.model.pojo.tv.TvShowGenres;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieActivity extends AppCompatActivity{

    public static final String EXTRA_FAVORITE_MOVIE = "extra_favorite_movie";
    public static final String EXTRA_FAVORITE_POSITION = "extra_favorite_position";

//    private boolean isEdit = false;

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private final String TAG = DetailMovieActivity.class.getSimpleName();
    private static final String API_KEY = BuildConfig.API_KEY;

    private ArrayList<MovieGenres> movieGenresList = new ArrayList<>();
    private MovieDetails detailsMovie = new MovieDetails();

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
    ProgressBar progressBar;

    //BUG FIXED//
    SharedPreferences preferences;
    private boolean isFavorite = false;
    String idMovie, title, poster, release, rating, category;
    //BUG FIXED//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        btnSaveFavorite = findViewById(R.id.btn_save_favorite);

        //BUG FIXED//
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //BUG FIXED//

        progressBar = findViewById(R.id.progress_load_detail);

        movieFavorite = getIntent().getParcelableExtra(EXTRA_FAVORITE_MOVIE);

        //BUG FIXED//
        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movieFavorite = new MovieFavorite(cursor);
                cursor.close();
            }
        }
        //BUG FIXED//


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
            if (movieFavorite.getCategory().equals("Movie")){
                moviesData();
            } else if (movieFavorite.getCategory().equals("Tv Show")) {
                tvShowData();
            }

        }

        btnSaveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //BUG FIXED//
                if (isFavorite) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(EXTRA_FAVORITE_MOVIE + movieFavorite.getIdMovie(), false);
                    editor.commit();
                    isFavorite = false;
                    Toast.makeText(DetailMovieActivity.this, "Favorite Dihapus", Toast.LENGTH_SHORT).show();
                    deleteFavorite();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(EXTRA_FAVORITE_MOVIE + movieFavorite.getIdMovie(), true);
                    editor.commit();
                    isFavorite = true;
                    Toast.makeText(DetailMovieActivity.this, "Favorite Disimpan", Toast.LENGTH_SHORT).show();
                    // saveFavorite();
                }
                updateFavorite();

            }
        });

        isFavorite = preferences.getBoolean(EXTRA_FAVORITE_MOVIE + movieFavorite.getIdMovie(), false);
        updateFavorite();
        //BUG FIXED//

    }

    private void updateFavorite() {
        if (isFavorite) {
            if (movieFavorite == null){
                btnSaveFavorite.setText("SUDAH DISIMPAN");
                btnSaveFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DetailMovieActivity.this, "Udh Masuk Favorite", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                btnSaveFavorite.setText("HAPUS");
            }
        } else {
            btnSaveFavorite.setText("SIMPAN");
        }
    }

    private void deleteFavorite() {
        try {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_FAVORITE_MOVIE, movieFavorite);
            getContentResolver().delete(getIntent().getData(), null, null);
            startActivityForResult(intent, DetailMovieActivity.RESULT_DELETE);
            Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.d(TAG, "a");
        }
    }

    private void moviesData() {
        progressBar.setVisibility(View.VISIBLE);
        movieFavorite = getIntent().getParcelableExtra(EXTRA_FAVORITE_MOVIE);
        int idMovie = Integer.parseInt(movieFavorite.getIdMovie());

        Log.d(TAG, "Load detailsMovie Movie");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        final Call<MovieDetails> detailCall = apiService.getDetailMovie(idMovie, API_KEY);

        detailCall.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.body() != null) {
                    detailsMovie = response.body();
                    movieGenresList = response.body().getMovieGenres();

                    Log.d(TAG, "Get detailsMovie Success :) ");
                    Toast.makeText(DetailMovieActivity.this, detailsMovie.getTitle(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(DetailMovieActivity.this, "Else", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d(TAG, "Get detailsMovie Failed :)");
                Toast.makeText(DetailMovieActivity.this, "Something Wrong When Parsing JSON", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        ButterKnife.bind(this);
    }

    private void tvShowData() {
        progressBar.setVisibility(View.VISIBLE);
        movieFavorite = getIntent().getParcelableExtra(EXTRA_FAVORITE_MOVIE);
        int idMovie = Integer.parseInt(movieFavorite.getIdMovie());

        Log.d(TAG, "Load detailsMovie Tv Show");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        final Call<TvShowDetails> detailTvCall = apiService.getDetailTv(idMovie, API_KEY);

        detailTvCall.enqueue(new Callback<TvShowDetails>() {
            @Override
            public void onResponse(Call<TvShowDetails> call, Response<TvShowDetails> response) {
                if (response.body() != null) {
                    detailsTv = response.body();
                    tvGenresList = response.body().getGenres();

                    Log.d(TAG, "Get detailsMovie Success :) ");
                    Toast.makeText(DetailMovieActivity.this, detailsTv.getName(), Toast.LENGTH_SHORT).show();

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

                    Glide.with(DetailMovieActivity.this)
                            .load("https://image.tmdb.org/t/p/w500" + detailsTv.getBackdropPath())
                            .into(imageMovieBackdrop);
                    Glide.with(DetailMovieActivity.this)
                            .load("https://image.tmdb.org/t/p/w154" + detailsTv.getPosterPath())
                            .into(imageMoviePoster);
                } else {
                    Log.d(TAG, "Get detailsMovie into else :) ");
                    Toast.makeText(DetailMovieActivity.this, "Else", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TvShowDetails> call, Throwable t) {
                Log.d(TAG, "Get detailsMovie Failed :)");
                Toast.makeText(DetailMovieActivity.this, "Something Wrong When Parsing JSON", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.btn_save_favorite) {
//
//            try {
//                Intent intent = new Intent();
//                intent.putExtra(EXTRA_FAVORITE_MOVIE, movieFavorite);
//                getContentResolver().delete(getIntent().getData(), null, null);
//                startActivityForResult(intent, DetailMovieActivity.RESULT_DELETE);
//                Toast.makeText(this, "deleted_successfully", Toast.LENGTH_SHORT).show();
//                finish();
//            } catch (Exception e) {
//                Log.d(TAG, "a");
//            }
//
//        }
//    }
}
