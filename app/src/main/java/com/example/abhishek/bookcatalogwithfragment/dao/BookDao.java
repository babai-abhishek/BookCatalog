package com.example.abhishek.bookcatalogwithfragment.dao;

import com.example.abhishek.bookcatalogwithfragment.models.bl.BookBusinessModel;
import com.example.abhishek.bookcatalogwithfragment.models.db.BookDbModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

/**
 * Created by abhishek on 31/3/18.
 */

public class BookDao {
    public static void save(final List<BookBusinessModel> bookBusinessModelList){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BookDbModel bookDbModel;
                for(BookBusinessModel bookBusinessModel: bookBusinessModelList){
                    bookDbModel = realm.where(BookDbModel.class)
                            .equalTo(BookDbModel.FIELD_ID, bookBusinessModel.getId()).findFirst();
                    if(bookDbModel==null){
                        bookDbModel = realm.createObject(BookDbModel.class, bookBusinessModel.getId());
                    }
                    bookDbModel.setName(bookBusinessModel.getName());
                    bookDbModel.setLanguage(bookBusinessModel.getLanguage());
                    bookDbModel.setPages(bookBusinessModel.getPages());
                    bookDbModel.setPublished(bookBusinessModel.getPublished());
                    bookDbModel.setAuthorId(bookBusinessModel.getAuthor().getId());
                    bookDbModel.setGenreId(bookBusinessModel.getGenre().getId());
                }
            }
        });
    }

    public static void save(BookBusinessModel bookBusinessModel){
        save(Collections.singletonList(bookBusinessModel));
    }

    public static List<BookBusinessModel> getAll(){
        final List<BookBusinessModel> bookBusinessModelList = new ArrayList<>();
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (BookDbModel dbModel: realm.where(BookDbModel.class).findAll()){
                    bookBusinessModelList.add(new BookBusinessModel(dbModel));
                }

            }
        });

        return bookBusinessModelList;
    }

    public static BookBusinessModel getOne(final String id){
        final BookBusinessModel[] result = {null};
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BookDbModel dbModel = realm.where(BookDbModel.class).equalTo(BookDbModel.FIELD_ID,id).findFirst();
                if(dbModel!= null){
                    result[0] = new BookBusinessModel(dbModel);
                }
            }
        });
        return result[0];
    }
}
