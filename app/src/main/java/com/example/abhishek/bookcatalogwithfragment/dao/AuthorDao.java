package com.example.abhishek.bookcatalogwithfragment.dao;

import com.example.abhishek.bookcatalogwithfragment.models.bl.AuthorBusinessModel;
import com.example.abhishek.bookcatalogwithfragment.models.db.AuthorDbModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

/**
 * Created by abhishek on 30/3/18.
 */

public class AuthorDao {

    public static void save(final List<AuthorBusinessModel> authorBusinessModelList){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AuthorDbModel authorDbModel;
                for(AuthorBusinessModel authorBusinessModel : authorBusinessModelList){
                    authorDbModel = realm.where(AuthorDbModel.class).equalTo(AuthorDbModel.FIELD_ID,
                            authorBusinessModel.getId()).findFirst();
                    if(authorDbModel==null){
                        authorDbModel = realm.createObject(AuthorDbModel.class, authorBusinessModel.getId());
//                        authorDbModel.setId(authorBusinessModel.getId());
                    }
                    authorDbModel.setName(authorBusinessModel.getName());
                    authorDbModel.setCountry(authorBusinessModel.getCountry());
                    authorDbModel.setLanguage(authorBusinessModel.getLanguage());
                }
            }
        });
    }

    public static void save(AuthorBusinessModel authorBusinessModel){
        save(Collections.singletonList(authorBusinessModel));
    }

    public static List<AuthorBusinessModel> getAll(){

        final List<AuthorBusinessModel> results = new ArrayList<>();
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(AuthorDbModel authorDbModel: realm.where(AuthorDbModel.class).findAll()){
                    results.add(new AuthorBusinessModel(authorDbModel));
                }
            }
        });

        return results;
    }

    public static AuthorBusinessModel getOne(final String id){

        final AuthorBusinessModel[] result = {null};

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AuthorDbModel authorDbModel = realm.where(AuthorDbModel.class).equalTo(AuthorDbModel.FIELD_ID, id)
                        .findFirst();
                if(authorDbModel !=null){
                    result[0] = new AuthorBusinessModel(authorDbModel);
                }
            }
        });

        return result[0];
    }
}
