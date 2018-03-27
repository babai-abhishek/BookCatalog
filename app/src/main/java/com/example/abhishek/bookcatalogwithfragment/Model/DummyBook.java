package com.example.abhishek.bookcatalogwithfragment.Model;

import java.io.Serializable;

/**
 * Created by abhishek on 27/3/18.
 */

public class DummyBook implements Serializable {

    private String imageUrl;
    private Book book;

    public DummyBook(String imageUrl, Book book) {
        this.imageUrl = imageUrl;
        this.book = book;
    }

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
}
