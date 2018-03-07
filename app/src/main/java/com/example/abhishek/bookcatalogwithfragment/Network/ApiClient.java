package com.example.abhishek.bookcatalogwithfragment.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by abhishek on 7/11/17.
 */

public class ApiClient {

    //public static final String BASE_URL = "https://book-directory.herokuapp.com/";
    public static final String BASE_URL = "http://ec2-52-91-254-15.compute-1.amazonaws.com:3000/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }
}
