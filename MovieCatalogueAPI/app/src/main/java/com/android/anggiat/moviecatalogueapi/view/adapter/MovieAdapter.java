package com.android.anggiat.moviecatalogueapi.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.anggiat.moviecatalogueapi.R;
import com.android.anggiat.moviecatalogueapi.model.pojo.Movie;
import com.android.anggiat.moviecatalogueapi.view.activity.DetailMovieActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private final Context context;
    private final ArrayList<Movie> movies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvName.setText(movies.get(position).getTitle());
        holder.tvReleaseDate.setText(movies.get(position).getReleaseDate());
        holder.tvRating.setText(movies.get(position).getVoteAverage());

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w154" + movies.get(position)
                .getPosterPath())
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntentDetail = new Intent(context, DetailMovieActivity.class);
                mIntentDetail.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position));
                context.startActivity(mIntentDetail);

            }
        });

    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_item_poster)
        ImageView ivPoster;
        @BindView(R.id.text_item_name)
        TextView tvName;
        @BindView(R.id.text_item_genre1)
        TextView tvReleaseDate;
        @BindView(R.id.text_item_rating)
        TextView tvRating;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
