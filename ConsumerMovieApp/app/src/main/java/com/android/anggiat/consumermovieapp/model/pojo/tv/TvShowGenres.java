package com.android.anggiat.consumermovieapp.model.pojo.tv;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TvShowGenres implements Parcelable {

    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public TvShowGenres() {
    }

    private TvShowGenres(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<TvShowGenres> CREATOR = new Parcelable.Creator<TvShowGenres>() {
        @Override
        public TvShowGenres createFromParcel(Parcel source) {
            return new TvShowGenres(source);
        }

        @Override
        public TvShowGenres[] newArray(int size) {
            return new TvShowGenres[size];
        }
    };

}
