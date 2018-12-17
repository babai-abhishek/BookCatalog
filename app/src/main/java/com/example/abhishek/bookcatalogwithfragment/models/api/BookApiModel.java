package com.example.abhishek.bookcatalogwithfragment.models.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek on 31/3/18.
 */

public class BookApiModel implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("published")
    @Expose
    private String published;

    @SerializedName("pages")
    @Expose
    private int pages;

    @SerializedName("authorId")
    @Expose
    private String authorId;

    @SerializedName("genreId")
    @Expose
    private String genreId;

    @SerializedName("id")
    @Expose
    private String id;

    public BookApiModel() {
    }


    protected BookApiModel(Parcel in) {
        name = in.readString();
        language = in.readString();
        published = in.readString();
        pages = in.readInt();
        authorId = in.readString();
        genreId = in.readString();
        id = in.readString();
    }

    public BookApiModel(String name, String language, String published, int pages, String authorId, String genreId, String id) {
        this.name = name;
        this.language = language;
        this.published = published;
        this.pages = pages;
        this.authorId = authorId;
        this.genreId = genreId;
        this.id = id;
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

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Creator<BookApiModel> CREATOR = new Creator<BookApiModel>() {
        @Override
        public BookApiModel createFromParcel(Parcel in) {
            return new BookApiModel(in);
        }

        @Override
        public BookApiModel[] newArray(int size) {
            return new BookApiModel[size];
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
        dest.writeString(published);
        dest.writeInt(pages);
        dest.writeString(authorId);
        dest.writeString(genreId);
        dest.writeString(id);
    }
}
