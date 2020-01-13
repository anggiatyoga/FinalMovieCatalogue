package com.android.anggiat.consumermovieapp.model.pojo.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieGenres implements Parcelable{

    @SerializedName("name")
    private String Name;
    @SerializedName("id")
    private int Id;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeInt(this.Id);
    }

    public MovieGenres() {
    }

    private MovieGenres(Parcel in) {
        this.Name = in.readString();
        this.Id = in.readInt();
    }

    public static final Parcelable.Creator<MovieGenres> CREATOR = new Parcelable.Creator<MovieGenres>() {
        @Override
        public MovieGenres createFromParcel(Parcel source) {
            return new MovieGenres(source);
        }

        @Override
        public MovieGenres[] newArray(int size) {
            return new MovieGenres[size];
        }
    };

}
