package com.android.anggiat.moviecatalogueapi.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.anggiat.moviecatalogueapi.BuildConfig;
import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.view.adapter.MovieAdapter;
import com.android.anggiat.moviecatalogueapi.view.activity.FavoriteActivity;
import com.android.anggiat.moviecatalogueapi.model.api.ApiClient;
import com.android.anggiat.moviecatalogueapi.model.api.ApiService;
import com.android.anggiat.moviecatalogueapi.model.pojo.Movie;
import com.android.anggiat.moviecatalogueapi.model.pojo.MovieResult;
import com.android.anggiat.moviecatalogueapi.view.activity.RemindersSettingActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment{

    private final String TAG = MovieFragment.class.getSimpleName();

    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    private Unbinder unbinder;
    @BindView(R.id.progress_load_movie)
    ProgressBar progressBar;
    private static final String API_KEY = BuildConfig.API_KEY;
    private ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movies");
        } else {
            progressBar = view.findViewById(R.id.progress_load_movie);
            getData("");
        }

        getActivity().setTitle(getString(R.string.title_movie));
        unbinder = ButterKnife.bind(this, view);

        movieAdapter = new MovieAdapter(getContext(), movies);
        rvMovie.setAdapter(movieAdapter);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void getData(final String query) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Trying to get connection");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<MovieResult> movieResultCall;

        if (query.length() > 0) {
            movieResultCall = apiService.getSearchMovie(query, API_KEY);
        } else {
            movieResultCall = apiService.getMovieList(API_KEY);
        }

        movieResultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                if (response.body() != null) {
                    movies = response.body().getResults();
                    Log.d(TAG, "Get data Success :) ");
                    MovieAdapter movieAdapter = new MovieAdapter(getContext(), movies);
                    rvMovie.setAdapter(movieAdapter);
                } else {
                    Log.d(TAG, "Get data Failed :)");
                    Toast.makeText(getContext(), R.string.failure, Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResult> call, @NonNull Throwable t) {
                Log.d(TAG, "Failure to get connection");
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_main, menu);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setQueryHint(getString(R.string.find_a_movie));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    getData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(newText);
                return false;
            }
        });
        searchItem.setActionView(searchView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings){
            Intent mIntentSetting = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntentSetting);
        } else if (item.getItemId() == R.id.action_favorite_movie) {
            Intent mIntentFavorite = new Intent(getContext(), FavoriteActivity.class);
            startActivity(mIntentFavorite);
        } else if (item.getItemId() == R.id.action_reminders_setting) {
            Intent mIntentRemindersSetting = new Intent(getContext(), RemindersSettingActivity.class);
            startActivity(mIntentRemindersSetting);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
    }

}
