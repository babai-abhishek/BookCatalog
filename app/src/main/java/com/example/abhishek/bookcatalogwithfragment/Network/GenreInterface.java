package com.example.abhishek.bookcatalogwithfragment.Network;


import com.example.abhishek.bookcatalogwithfragment.models.api.GenreApiModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by abhishek on 7/11/17.
 */

public interface GenreInterface {
    @GET("/genres")
    Call<List<GenreApiModel>> getAllGenres();

    @GET("/genres/{id}")
    Call<GenreApiModel> getGenre(@Path("id") String id);

    @POST("/genres")
    Call<GenreApiModel> newGenreEntry(@Body GenreApiModel genre);

    @DELETE("/genres/{id}")
    Call<ResponseBody> deleteGenreEntry(@Path("id") String id);

    @PUT("/genres/{id}")
    Call<GenreApiModel> updateGenreEntry(@Path("id") String id, @Body GenreApiModel genre);
}
