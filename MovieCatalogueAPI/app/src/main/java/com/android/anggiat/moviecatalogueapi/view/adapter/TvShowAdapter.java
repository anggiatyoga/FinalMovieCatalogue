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
import com.android.anggiat.moviecatalogueapi.model.pojo.TvShow;
import com.android.anggiat.moviecatalogueapi.view.activity.DetailTvActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<TvShow> tvShows;

    public TvShowAdapter(Context context, ArrayList<TvShow> tvShows) {
        this.context = context;
        this.tvShows = tvShows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvName.setText(tvShows.get(position).getName());
        holder.tvReleaseDate.setText(tvShows.get(position).getFirstAirDate());
        holder.tvRating.setText(tvShows.get(position).getVoteAverage());

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w154" + tvShows.get(position)
                        .getPosterPath())
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TvShow tvShow = new TvShow();
                tvShow.setId(tvShows.get(position).getId());

                Intent mIntentDetail = new Intent(context, DetailTvActivity.class);
                mIntentDetail.putExtra(DetailTvActivity.EXTRA_TV, tvShow);
                context.startActivity(mIntentDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShows == null ? 0 : tvShows.size();
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
