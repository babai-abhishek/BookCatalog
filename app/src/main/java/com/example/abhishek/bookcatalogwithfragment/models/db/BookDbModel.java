package com.example.abhishek.bookcatalogwithfragment.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

/**
 * Created by abhishek on 31/3/18.
 */

public class BookDbModel extends RealmObject {

    public final static String FIELD_NAME="name";
    public final static String FIELD_ID="id";
    public final static String FIELD_LANGUAGE="language";
    public final static String FIELD_PUBLISHDATE="country";
    public final static String FIELD_PAGES="pages";
    public final static String FIELD_AUTHORID="authorId";
    public final static String FIELD_GENREID="genreId";

    @RealmField(name = FIELD_NAME)
    private String name;

    @RealmField(name = FIELD_LANGUAGE)
    private String language;

    @RealmField(name = FIELD_PUBLISHDATE)
    private String published;

    @RealmField(name = FIELD_PAGES)
    private int pages;

    @RealmField(name = FIELD_AUTHORID)
    private String authorId;

    @RealmField(name = FIELD_GENREID)
    private String genreId;

    /*private GenreBusinessModel genre;
    private AuthorBusinessModel author;*/

    @RealmField(name = FIELD_ID)
    @PrimaryKey
    private String id;

    public String getName() {
        return name;
    }

    public BookDbModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public BookDbModel setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getPublished() {
        return published;
    }

    public BookDbModel setPublished(String published) {
        this.published = published;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public BookDbModel setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public String getAuthorId() {
        return authorId;
    }

    public BookDbModel setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getGenreId() {
        return genreId;
    }

    public BookDbModel setGenreId(String genreId) {
        this.genreId = genreId;
        return this;
    }

    public String getId() {
        return id;
    }

    public BookDbModel setId(String id) {
        this.id = id;
        return this;
    }
}
