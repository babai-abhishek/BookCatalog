package com.example.abhishek.bookcatalogwithfragment.models.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;

/**
 * Created by abhishek on 30/3/18.
 */

public class GenreApiModel implements Parcelable{


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    @PrimaryKey
    private String id;

    public GenreApiModel(){}

    public GenreApiModel(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public GenreApiModel(Parcel in) {
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<GenreApiModel> CREATOR = new Creator<GenreApiModel>() {
        @Override
        public GenreApiModel createFromParcel(Parcel in) {
            return new GenreApiModel(in);
        }

        @Override
        public GenreApiModel[] newArray(int size) {
            return new GenreApiModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
    }

}
