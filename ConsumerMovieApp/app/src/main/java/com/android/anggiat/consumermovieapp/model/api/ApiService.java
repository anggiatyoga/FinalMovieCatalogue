package com.android.anggiat.consumermovieapp.model.api;

import com.android.anggiat.consumermovieapp.model.pojo.movie.MovieDetails;
import com.android.anggiat.consumermovieapp.model.pojo.movie.MovieResult;
import com.android.anggiat.consumermovieapp.model.pojo.tv.TvShowDetails;
import com.android.anggiat.consumermovieapp.model.pojo.tv.TvShowResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

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

}
