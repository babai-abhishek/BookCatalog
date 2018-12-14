package com.example.abhishek.bookcatalogwithfragment.models.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek on 30/3/18.
 */

public class AuthorApiModel implements Parcelable{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("id")
    @Expose
    private String id;

    public AuthorApiModel() {
    }

    public AuthorApiModel(String id, String name, String language, String country) {
        this.name = name;
        this.language = language;
        this.country = country;
        this.id = id;
    }

    protected AuthorApiModel(Parcel in) {
        name = in.readString();
        language = in.readString();
        country = in.readString();
        id = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Creator<AuthorApiModel> CREATOR = new Creator<AuthorApiModel>() {
        @Override
        public AuthorApiModel createFromParcel(Parcel in) {
            return new AuthorApiModel(in);
        }

        @Override
        public AuthorApiModel[] newArray(int size) {
            return new AuthorApiModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(language);
        dest.writeString(country);
        dest.writeString(id);
    }
}
