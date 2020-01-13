package com.android.anggiat.moviecatalogueapi.database.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.CATEGORY;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.ID_MOVIE;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.POSTER;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.RATING;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.RELEASE;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.MovieFavoriteColumns.TITLE;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.getColumnInt;
import static com.android.anggiat.moviecatalogueapi.database.db.DatabaseContract.getColumnString;

public class MovieFavorite implements Parcelable {

    private int id;
    private String idMovie;
    private String poster;
    private String title;
    private String release;
    private String rating;
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.idMovie);
        dest.writeString(this.poster);
        dest.writeString(this.title);
        dest.writeString(this.release);
        dest.writeString(this.rating);
        dest.writeString(this.category);
    }

    public MovieFavorite() {
    }

    public MovieFavorite(int id, String idMovie, String poster, String title, String release, String rating, String category) {
        this.id = id;
        this.idMovie = idMovie;
        this.poster = poster;
        this.title = title;
        this.release = release;
        this.rating = rating;
        this.category = category;
    }

    public MovieFavorite(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.idMovie = getColumnString(cursor, ID_MOVIE);
        this.poster = getColumnString(cursor, POSTER);
        this.title = getColumnString(cursor, TITLE);
        this.release = getColumnString(cursor, RELEASE);
        this.rating = getColumnString(cursor, RATING);
        this.category = getColumnString(cursor, CATEGORY);
    }

    private MovieFavorite(Parcel in) {
        this.id = in.readInt();
        this.idMovie = in.readString();
        this.poster = in.readString();
        this.title = in.readString();
        this.release = in.readString();
        this.rating = in.readString();
        this.category = in.readString();
    }

    public static final Creator<MovieFavorite> CREATOR = new Creator<MovieFavorite>() {
        @Override
        public MovieFavorite createFromParcel(Parcel source) {
            return new MovieFavorite(source);
        }

        @Override
        public MovieFavorite[] newArray(int size) {
            return new MovieFavorite[size];
        }
    };
}
