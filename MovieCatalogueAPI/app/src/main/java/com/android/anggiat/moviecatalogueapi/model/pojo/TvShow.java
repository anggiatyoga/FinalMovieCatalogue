package com.android.anggiat.moviecatalogueapi.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TvShow implements Parcelable {

    @SerializedName("original_name")
    private String originalName;

    @SerializedName("name")
    private String name;

    @SerializedName("popularity")
    private String popularity;

    @SerializedName("vote_count")
    private String vote_count;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("id")
    private int id;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalName);
        dest.writeString(this.name);
        dest.writeString(this.popularity);
        dest.writeString(this.vote_count);
        dest.writeString(this.firstAirDate);
        dest.writeString(this.backdropPath);
        dest.writeString(this.originalLanguage);
        dest.writeInt(this.id);
        dest.writeString(this.voteAverage);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
    }

    public TvShow() {
    }

    private TvShow(Parcel in) {
        this.originalName = in.readString();
        this.name = in.readString();
        this.popularity = in.readString();
        this.vote_count = in.readString();
        this.firstAirDate = in.readString();
        this.backdropPath = in.readString();
        this.originalLanguage = in.readString();
        this.id = in.readInt();
        this.voteAverage = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel source) {
            return new TvShow(source);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

}
