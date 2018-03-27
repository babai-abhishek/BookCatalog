package com.example.abhishek.bookcatalogwithfragment.Model;

import java.util.ArrayList;

/**
 * Created by abhishek on 27/3/18.
 */

public class DummyBookImageUrls {

    public static String getImageUrl(int i){
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://api.androidhive.info/json/movies/1.jpg");
        urls.add("http://api.androidhive.info/json/movies/2.jpg");
        urls.add("http://api.androidhive.info/json/movies/3.jpg");
        urls.add("http://api.androidhive.info/json/movies/4.jpg");
        urls.add("http://api.androidhive.info/json/movies/5.jpg");
        urls.add("http://api.androidhive.info/json/movies/6.jpg");
        urls.add("http://api.androidhive.info/json/movies/7.jpg");
        urls.add("http://api.androidhive.info/json/movies/8.jpg");
        urls.add("http://api.androidhive.info/json/movies/9.jpg");
        urls.add("http://api.androidhive.info/json/movies/10.jpg");
        urls.add("http://api.androidhive.info/json/movies/11.jpg");
        urls.add("http://api.androidhive.info/json/movies/12.jpg");
        urls.add("http://api.androidhive.info/json/movies/13.jpg");
        urls.add("http://api.androidhive.info/json/movies/14.jpg");
        urls.add("http://api.androidhive.info/json/movies/15.jpg");


        return urls.get(i);
    }
}
