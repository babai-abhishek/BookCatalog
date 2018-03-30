package com.example.abhishek.bookcatalogwithfragment.models.bl;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.abhishek.bookcatalogwithfragment.models.api.AuthorApiModel;
import com.example.abhishek.bookcatalogwithfragment.models.db.AuthorDbModel;

/**
 * Created by abhishek on 30/3/18.
 */

public class AuthorBusinessModel implements Parcelable{

    private String name;
    private String language;
    private String country;
    private String id;

    public AuthorBusinessModel() {
    }

    public AuthorBusinessModel(AuthorApiModel authorApiModel) {
        this.name = authorApiModel.getName();
        this.country = authorApiModel.getCountry();
        this.language = authorApiModel.getLanguage();
        this.id = authorApiModel.getId();
    }

    public AuthorBusinessModel(AuthorDbModel authorDbModel) {
        this.name = authorDbModel.getName();
        this.country = authorDbModel.getCountry();
        this.language = authorDbModel.getLanguage();
        this.id = authorDbModel.getId();
    }

    protected AuthorBusinessModel(Parcel in) {
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

    public static final Creator<AuthorBusinessModel> CREATOR = new Creator<AuthorBusinessModel>() {
        @Override
        public AuthorBusinessModel createFromParcel(Parcel in) {
            return new AuthorBusinessModel(in);
        }

        @Override
        public AuthorBusinessModel[] newArray(int size) {
            return new AuthorBusinessModel[size];
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
