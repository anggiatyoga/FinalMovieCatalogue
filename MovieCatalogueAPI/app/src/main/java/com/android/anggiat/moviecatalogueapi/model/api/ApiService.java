package com.android.anggiat.moviecatalogueapi.model.api;

import com.android.anggiat.moviecatalogueapi.model.pojo.MovieDetails;
import com.android.anggiat.moviecatalogueapi.model.pojo.MovieResult;
import com.android.anggiat.moviecatalogueapi.model.pojo.TvShowDetails;
import com.android.anggiat.moviecatalogueapi.model.pojo.TvShowResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("discover/movie")
    Call<MovieResult> getMovieList(
            @Query("api_key") String API_KEY
    );

    @GET("discover/tv")
    Call<TvShowResult> getTvShowList(
            @Query("api_key") String API_KEY
    );

    @GET("movie/{movie_id}")
    Call<MovieDetails> getDetailMovie(
            @Path("movie_id") int id,
            @Query("api_key") String API_KEY
    );

    @GET("tv/{tv_id}")
    Call<TvShowDetails> getDetailTv(
            @Path("tv_id") int id,
            @Query("api_key") String API_KEY
    );

    @GET("search/movie/")
    Call<MovieResult> getSearchMovie(
            @Query("query") String query,
            @Query("api_key") String API_KEY
    );

    @GET("search/tv/")
    Call<TvShowResult> getSearchTv(
            @Query("query") String query,
            @Query("api_key") String API_KEY
    );

    @GET("discover/movie")
    Call<MovieResult> getReleaseMovie(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String gte,
            @Query("primary_release_date.lte") String lte);

}
