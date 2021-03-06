package com.android.anggiat.consumermovieapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.anggiat.consumermovieapp.view.activity.DetailMovieActivity;
import com.android.anggiat.consumermovieapp.R;
import com.android.anggiat.consumermovieapp.entity.MovieFavorite;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.anggiat.consumermovieapp.database.DatabaseContract.MovieFavoriteColumns.CONTENT_URI;

public class ConsumerAdapter extends RecyclerView.Adapter<ConsumerAdapter.MovieFavoriteViewHolder> {

    private ArrayList<MovieFavorite> movieFavoriteArrayList = new ArrayList<>();
    private Activity activity;

    public ConsumerAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<MovieFavorite> getMovieFavoriteArrayList() {
        return movieFavoriteArrayList;
    }

    public void setMovieFavoriteArrayList(ArrayList<MovieFavorite> movieFavoriteArrayList) {
        if (movieFavoriteArrayList.size() > 0 ){
            this.movieFavoriteArrayList.clear();
        }
        this.movieFavoriteArrayList.addAll(movieFavoriteArrayList);
        notifyDataSetChanged();
    }

    public void addItem(MovieFavorite movieFavorite) {
        this.movieFavoriteArrayList.add(movieFavorite);
        notifyItemInserted(movieFavoriteArrayList.size() - 1);
    }

    public void updateItem(int position, MovieFavorite movieFavorite) {
        this.movieFavoriteArrayList.set(position, movieFavorite);
        notifyItemChanged(position, movieFavorite);
    }

    public void removeItem(int position) {
        this.movieFavoriteArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movieFavoriteArrayList.size());
    }

    @NonNull
    @Override
    public ConsumerAdapter.MovieFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_movie, parent, false);
        return new MovieFavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumerAdapter.MovieFavoriteViewHolder holder, final int position) {
        holder.tvName.setText(getMovieFavoriteArrayList().get(position).getTitle());
        holder.tvReleaseDate.setText(getMovieFavoriteArrayList().get(position).getRelease());
        holder.tvRating.setText(getMovieFavoriteArrayList().get(position).getRating());
        holder.tvCategory.setText(getMovieFavoriteArrayList().get(position).getCategory());

        Glide.with(activity)
                .load("https://image.tmdb.org/t/p/w154" + getMovieFavoriteArrayList().get(position).getPoster())
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailMovieActivity.class);

                Uri uri = Uri.parse(CONTENT_URI + "/" + getMovieFavoriteArrayList().get(position).getId());
                intent.setData(uri);
                intent.putExtra(DetailMovieActivity.EXTRA_FAVORITE_POSITION, position);
                intent.putExtra(DetailMovieActivity.EXTRA_FAVORITE_MOVIE, getMovieFavoriteArrayList().get(position));
                activity.startActivityForResult(intent, DetailMovieActivity.REQUEST_UPDATE) ;
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieFavoriteArrayList.size();
    }

    public class MovieFavoriteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_item_poster)
        ImageView ivPoster;
        @BindView(R.id.text_item_name)
        TextView tvName;
        @BindView(R.id.text_item_genre1)
        TextView tvReleaseDate;
        @BindView(R.id.text_item_rating)
        TextView tvRating;
        @BindView(R.id.text_movie_category)
        TextView tvCategory;


        public MovieFavoriteViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
