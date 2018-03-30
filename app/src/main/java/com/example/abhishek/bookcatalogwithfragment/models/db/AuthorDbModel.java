package com.example.abhishek.bookcatalogwithfragment.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

/**
 * Created by abhishek on 30/3/18.
 */

public class AuthorDbModel extends RealmObject {

    public final static String FIELD_NAME="name";
    public final static String FIELD_ID="id";
    public final static String FIELD_LANGUAGE="language";
    public final static String FIELD_COUNTRY="country";

    @RealmField(name = FIELD_NAME)
    private String name;

    @RealmField(name = FIELD_LANGUAGE)
    private String language;

    @RealmField(name = FIELD_COUNTRY)
    private String country;

    @RealmField(name = FIELD_ID)
    @PrimaryKey
    private String id;

    public String getName() {
        return name;
    }

    public AuthorDbModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public AuthorDbModel setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public AuthorDbModel setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getId() {
        return id;
    }

    public AuthorDbModel setId(String id) {
        this.id = id;
        return this;
    }
}
