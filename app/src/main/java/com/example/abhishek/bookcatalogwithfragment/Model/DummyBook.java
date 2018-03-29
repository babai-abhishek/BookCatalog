package com.example.abhishek.bookcatalogwithfragment.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by abhishek on 27/3/18.
 */

public class DummyBook implements Serializable, Parcelable {

    private String imageUrl;
    private Book book;

    public DummyBook() {
    }

    public DummyBook(String imageUrl, Book book) {
        this.imageUrl = imageUrl;
        this.book = book;
    }

    protected DummyBook(Parcel in) {
        imageUrl = in.readString();
        book = in.readParcelable(Book.class.getClassLoader());
    }

    public static final Creator<DummyBook> CREATOR = new Creator<DummyBook>() {
        @Override
        public DummyBook createFromParcel(Parcel in) {
            return new DummyBook(in);
        }

        @Override
        public DummyBook[] newArray(int size) {
            return new DummyBook[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeParcelable(book, flags);
    }
}
