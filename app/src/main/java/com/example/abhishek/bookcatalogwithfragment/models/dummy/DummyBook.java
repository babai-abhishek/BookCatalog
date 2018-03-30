package com.example.abhishek.bookcatalogwithfragment.models.dummy;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.abhishek.bookcatalogwithfragment.models.bl.BookBusinessModel;

import java.io.Serializable;

/**
 * Created by abhishek on 27/3/18.
 */

public class DummyBook implements Serializable, Parcelable {

    private String imageUrl;
    private BookBusinessModel book;

    public DummyBook() {
    }

    public DummyBook(String imageUrl, BookBusinessModel book) {
        this.imageUrl = imageUrl;
        this.book = book;
    }

    protected DummyBook(Parcel in) {
        imageUrl = in.readString();
        book = in.readParcelable(BookBusinessModel.class.getClassLoader());
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

    public BookBusinessModel getBook() {
        return book;
    }

    public void setBook(BookBusinessModel book) {
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
