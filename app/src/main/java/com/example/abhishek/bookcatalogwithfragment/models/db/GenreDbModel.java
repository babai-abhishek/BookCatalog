package com.example.abhishek.bookcatalogwithfragment.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

/**
 * Created by abhishek on 30/3/18.
 */

public class GenreDbModel extends RealmObject {

    public final static String FIELD_NAME="name";
    public final static String FIELD_ID="id";

    @RealmField(name = FIELD_NAME)
    private String name;

    @PrimaryKey
    @RealmField(name = FIELD_ID)
    private String id;

    public GenreDbModel() {
    }

    public String getName() {
        return name;
    }

    public GenreDbModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public GenreDbModel setId(String id) {
        this.id = id;
        return this;
    }
}
