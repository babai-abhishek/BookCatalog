package com.example.abhishek.bookcatalogwithfragment.dao;

import android.support.annotation.NonNull;

import com.example.abhishek.bookcatalogwithfragment.models.bl.GenreBusinessModel;
import com.example.abhishek.bookcatalogwithfragment.models.db.GenreDbModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

/**
 * Created by abhishek on 30/3/18.
 */

public class GenreDao {

    public static void save(final List<GenreBusinessModel> genreBusinessModelList){
        //TODO
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                GenreDbModel genreDbModel;
                for(GenreBusinessModel businessModel : genreBusinessModelList){
                    genreDbModel = realm.where(GenreDbModel.class).equalTo(GenreDbModel.FIELD_ID, businessModel.getId()).findFirst();
                    if(genreDbModel==null){
                        genreDbModel=realm.createObject(GenreDbModel.class, businessModel.getId());
                      //  genreDbModel.setId(businessModel.getId());
                    }
                    genreDbModel.setName(businessModel.getName());
                }
            }
        });
    }

    public static void save(GenreBusinessModel genreBusinessModel){
        save(Collections.singletonList(genreBusinessModel));
    }

    public static List<GenreBusinessModel> getAll(){
        //TODO
        final List<GenreBusinessModel> results=new ArrayList<>();
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(GenreDbModel dbModel : realm.where(GenreDbModel.class).findAll()){
                    results.add(new GenreBusinessModel(dbModel));
                }
            }
        });
        return results;
    }

    public static GenreBusinessModel getOne(final String id){
        //TODO
        final GenreBusinessModel[] result = {null};
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                GenreDbModel genreDbModel = realm.where(GenreDbModel.class).equalTo(GenreDbModel.FIELD_ID, id).findFirst();
                if(genreDbModel !=null)
                    result[0] =new GenreBusinessModel(genreDbModel);
            }
        });
        return result[0];
    }
}
