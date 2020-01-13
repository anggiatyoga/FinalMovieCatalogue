package com.android.anggiat.moviecatalogueapi.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class MovieDetails implements Parcelable {

    @SerializedName("vote_count")
    private int VoteCount;
    @SerializedName("vote_average")
    private String VoteAverage;
    @SerializedName("video")
    private boolean Video;
    @SerializedName("title")
    private String Title;
    @SerializedName("tagline")
    private String Tagline;
    @SerializedName("status")
    private String Status;
    @SerializedName("runtime")
    private String Runtime;
    @SerializedName("revenue")
    private String Revenue;
    @SerializedName("release_date")
    private String ReleaseDate;
    @SerializedName("poster_path")
    private String PosterPath;
    @SerializedName("popularity")
    private double Popularity;
    @SerializedName("overview")
    private String Overview;
    @SerializedName("original_title")
    private String OriginalTitle;
    @SerializedName("original_language")
    private String OriginalLanguage;
    @SerializedName("imdb_id")
    private String ImdbId;
    @SerializedName("id")
    private int Id;
    @SerializedName("homepage")
    private String Homepage;
    @SerializedName("genres")
    private ArrayList<MovieGenres> MovieGenres;
    @SerializedName("budget")
    private int Budget;
    @SerializedName("backdrop_path")
    private String BackdropPath;
    @SerializedName("adult")
    private boolean Adult;

    public int getVoteCount() {
        return VoteCount;
    }

    public void setVoteCount(int VoteCount) {
        this.VoteCount = VoteCount;
    }

    public String getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(String VoteAverage) {
        this.VoteAverage = VoteAverage;
    }

    public boolean getVideo() {
        return Video;
    }

    public void setVideo(boolean Video) {
        this.Video = Video;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getTagline() {
        return Tagline;
    }

    public void setTagline(String Tagline) {
        this.Tagline = Tagline;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String Runtime) {
        this.Runtime = Runtime;
    }

    public String getRevenue() {
        return Revenue;
    }

    public void setRevenue(String Revenue) {
        this.Revenue = Revenue;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String ReleaseDate) {
        this.ReleaseDate = ReleaseDate;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String PosterPath) {
        this.PosterPath = PosterPath;
    }

    public double getPopularity() {
        return Popularity;
    }

    public void setPopularity(double Popularity) {
        this.Popularity = Popularity;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String Overview) {
        this.Overview = Overview;
    }

    public String getOriginalTitle() {
        return OriginalTitle;
    }

    public void setOriginalTitle(String OriginalTitle) {
        this.OriginalTitle = OriginalTitle;
    }

    public String getOriginalLanguage() {
        return OriginalLanguage;
    }

    public void setOriginalLanguage(String OriginalLanguage) {
        this.OriginalLanguage = OriginalLanguage;
    }

    public String getImdbId() {
        return ImdbId;
    }

    public void setImdbId(String ImdbId) {
        this.ImdbId = ImdbId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getHomepage() {
        return Homepage;
    }

    public void setHomepage(String Homepage) {
        this.Homepage = Homepage;
    }

    public ArrayList<MovieGenres> getMovieGenres() {
        return MovieGenres;
    }

    public void setMovieGenres(ArrayList<MovieGenres> MovieGenres) {
        this.MovieGenres = MovieGenres;
    }

    public int getBudget() {
        return Budget;
    }

    public void setBudget(int Budget) {
        this.Budget = Budget;
    }

    public String getBackdropPath() {
        return BackdropPath;
    }

    public void setBackdropPath(String BackdropPath) {
        this.BackdropPath = BackdropPath;
    }

    public boolean getAdult() {
        return Adult;
    }

    public void setAdult(boolean Adult) {
        this.Adult = Adult;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.VoteCount);
        dest.writeString(this.VoteAverage);
        dest.writeByte(this.Video ? (byte) 1 : (byte) 0);
        dest.writeString(this.Title);
        dest.writeString(this.Tagline);
        dest.writeString(this.Status);
        dest.writeString(this.Runtime);
        dest.writeString(this.Revenue);
        dest.writeString(this.ReleaseDate);
        dest.writeString(this.PosterPath);
        dest.writeDouble(this.Popularity);
        dest.writeString(this.Overview);
        dest.writeString(this.OriginalTitle);
        dest.writeString(this.OriginalLanguage);
        dest.writeString(this.ImdbId);
        dest.writeInt(this.Id);
        dest.writeString(this.Homepage);
        dest.writeTypedList(this.MovieGenres);
        dest.writeInt(this.Budget);
        dest.writeString(this.BackdropPath);
        dest.writeByte(this.Adult ? (byte) 1 : (byte) 0);
    }

    public MovieDetails() {
    }

    private MovieDetails(Parcel in) {
        this.VoteCount = in.readInt();
        this.VoteAverage = in.readString();
        this.Video = in.readByte() != 0;
        this.Title = in.readString();
        this.Tagline = in.readString();
        this.Status = in.readString();
        this.Runtime = in.readString();
        this.Revenue = in.readString();
        this.ReleaseDate = in.readString();
        this.PosterPath = in.readString();
        this.Popularity = in.readDouble();
        this.Overview = in.readString();
        this.OriginalTitle = in.readString();
        this.OriginalLanguage = in.readString();
        this.ImdbId = in.readString();
        this.Id = in.readInt();
        this.Homepage = in.readString();
        this.MovieGenres = in.createTypedArrayList(com.android.anggiat.moviecatalogueapi.model.pojo.MovieGenres.CREATOR);
        this.Budget = in.readInt();
        this.BackdropPath = in.readString();
        this.Adult = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel source) {
            return new MovieDetails(source);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

}
