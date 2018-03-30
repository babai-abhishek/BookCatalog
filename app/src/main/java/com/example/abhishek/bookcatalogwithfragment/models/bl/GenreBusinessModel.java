package com.example.abhishek.bookcatalogwithfragment.models.bl;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.abhishek.bookcatalogwithfragment.models.api.GenreApiModel;
import com.example.abhishek.bookcatalogwithfragment.models.db.GenreDbModel;

/**
 * Created by abhishek on 30/3/18.
 */

public class GenreBusinessModel implements Parcelable {
    private String name;
    private String id;

    public GenreBusinessModel() {
    }

    public GenreBusinessModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public GenreBusinessModel(GenreApiModel apiModel) {
        this.id=apiModel.getId();
        this.name=apiModel.getName();
    }
    public GenreBusinessModel(GenreDbModel dbModel) {
        this.id=dbModel.getId();
        this.name=dbModel.getName();
    }
    public GenreBusinessModel(Parcel in) {
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<GenreBusinessModel> CREATOR = new Creator<GenreBusinessModel>() {
        @Override
        public GenreBusinessModel createFromParcel(Parcel in) {
            return new GenreBusinessModel(in);
        }

        @Override
        public GenreBusinessModel[] newArray(int size) {
            return new GenreBusinessModel[size];
        }
    };

    public GenreApiModel getAsApiModel(){
        return new GenreApiModel(this.id, this.name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
    }
}
