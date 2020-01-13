package com.android.anggiat.consumermovieapp.model.pojo.tv;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TvShowDetails implements Parcelable{

    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("type")
    private String type;
    @SerializedName("status")
    private String status;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("overview")
    private String overview;
    @SerializedName("original_name")
    private String originalName;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("origin_country")
    private List<String> originCountry;
    @SerializedName("number_of_seasons")
    private int numberOfSeasons;
    @SerializedName("number_of_episodes")
    private int numberOfEpisodes;
    @SerializedName("name")
    private String name;
    @SerializedName("last_air_date")
    private String lastAirDate;
    @SerializedName("languages")
    private List<String> languages;
    @SerializedName("in_production")
    private boolean inProduction;
    @SerializedName("id")
    private int id;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("genres")
    private ArrayList<TvShowGenres> genres;
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("episode_run_time")
    private List<Integer> episodeRunTime;
    @SerializedName("backdrop_path")
    private String backdropPath;

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public boolean getInProduction() {
        return inProduction;
    }

    public void setInProduction(boolean inProduction) {
        this.inProduction = inProduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public ArrayList<TvShowGenres> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<TvShowGenres> genres) {
        this.genres = genres;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.type);
        dest.writeString(this.status);
        dest.writeString(this.posterPath);
        dest.writeDouble(this.popularity);
        dest.writeString(this.overview);
        dest.writeString(this.originalName);
        dest.writeString(this.originalLanguage);
        dest.writeStringList(this.originCountry);
        dest.writeInt(this.numberOfSeasons);
        dest.writeInt(this.numberOfEpisodes);
        dest.writeString(this.name);
        dest.writeString(this.lastAirDate);
        dest.writeStringList(this.languages);
        dest.writeByte(this.inProduction ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
        dest.writeString(this.homepage);
        dest.writeTypedList(this.genres);
        dest.writeString(this.firstAirDate);
        dest.writeList(this.episodeRunTime);
        dest.writeString(this.backdropPath);
    }

    public TvShowDetails() {
    }

    private TvShowDetails(Parcel in) {
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
        this.type = in.readString();
        this.status = in.readString();
        this.posterPath = in.readString();
        this.popularity = in.readDouble();
        this.overview = in.readString();
        this.originalName = in.readString();
        this.originalLanguage = in.readString();
        this.originCountry = in.createStringArrayList();
        this.numberOfSeasons = in.readInt();
        this.numberOfEpisodes = in.readInt();
        this.name = in.readString();
        this.lastAirDate = in.readString();
        this.languages = in.createStringArrayList();
        this.inProduction = in.readByte() != 0;
        this.id = in.readInt();
        this.homepage = in.readString();
        this.genres = in.createTypedArrayList(TvShowGenres.CREATOR);
        this.firstAirDate = in.readString();
        this.episodeRunTime = new ArrayList<>();
        in.readList(this.episodeRunTime, Integer.class.getClassLoader());
        this.backdropPath = in.readString();
    }

    public static final Parcelable.Creator<TvShowDetails> CREATOR = new Parcelable.Creator<TvShowDetails>() {
        @Override
        public TvShowDetails createFromParcel(Parcel source) {
            return new TvShowDetails(source);
        }

        @Override
        public TvShowDetails[] newArray(int size) {
            return new TvShowDetails[size];
        }
    };

}
