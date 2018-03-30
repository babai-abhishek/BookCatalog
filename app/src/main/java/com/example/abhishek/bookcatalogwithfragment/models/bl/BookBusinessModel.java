package com.example.abhishek.bookcatalogwithfragment.models.bl;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.abhishek.bookcatalogwithfragment.models.api.BookApiModel;
import com.example.abhishek.bookcatalogwithfragment.models.db.BookDbModel;

import io.realm.annotations.PrimaryKey;

/**
 * Created by abhishek on 31/3/18.
 */

public class BookBusinessModel implements Parcelable {

    private String name;
    private String language;
    private String published;
    private int pages;
    /* private String authorId;
     private String genreId;*/
    private GenreBusinessModel genre;
    private AuthorBusinessModel author;
    private String id;

    public BookBusinessModel() {
    }

    public BookBusinessModel(BookDbModel bookDbModel) {
        this.name = bookDbModel.getName();
        this.language = bookDbModel.getLanguage();
        this.published = bookDbModel.getPublished();
        this.pages = bookDbModel.getPages();
        this.id = bookDbModel.getId();
        this.genre = new GenreBusinessModel(null, bookDbModel.getId());
        this.author = new AuthorBusinessModel(null, null, null, bookDbModel.getId());
    }

    public BookBusinessModel(BookApiModel bookApiModel) {
        this.name = bookApiModel.getName();
        this.language = bookApiModel.getLanguage();
        this.published = bookApiModel.getPublished();
        this.pages = bookApiModel.getPages();
        this.id = bookApiModel.getId();
        this.genre = new GenreBusinessModel(null, bookApiModel.getId());
        this.author = new AuthorBusinessModel(null, null, null, bookApiModel.getId());
    }

    protected BookBusinessModel(Parcel in) {
        name = in.readString();
        language = in.readString();
        published = in.readString();
        pages = in.readInt();
        genre = in.readParcelable(GenreBusinessModel.class.getClassLoader());
        author = in.readParcelable(AuthorBusinessModel.class.getClassLoader());
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

    public GenreBusinessModel getGenre() {
        return genre;
    }

    public void setGenre(GenreBusinessModel genre) {
        this.genre = genre;
    }

    public AuthorBusinessModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBusinessModel author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(language);
        dest.writeString(published);
        dest.writeInt(pages);
        dest.writeParcelable(genre, flags);
        dest.writeParcelable(author, flags);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookBusinessModel> CREATOR = new Creator<BookBusinessModel>() {
        @Override
        public BookBusinessModel createFromParcel(Parcel in) {
            return new BookBusinessModel(in);
        }

        @Override
        public BookBusinessModel[] newArray(int size) {
            return new BookBusinessModel[size];
        }
    };
}
