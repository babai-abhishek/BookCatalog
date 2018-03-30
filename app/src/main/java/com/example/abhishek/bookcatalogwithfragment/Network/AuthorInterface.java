package com.example.abhishek.bookcatalogwithfragment.Network;


import com.example.abhishek.bookcatalogwithfragment.models.api.AuthorApiModel;

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

public interface AuthorInterface {

    @GET("/authors")
    Call<List<AuthorApiModel>> getAllAuthors();

    @GET("/authors/{id}")
    Call<AuthorApiModel> getAuthor(@Path("id") String id);

    @POST("/authors")
    Call<AuthorApiModel> newAuthorEntry(@Body AuthorApiModel author);

    @DELETE("/authors/{id}")
    Call<ResponseBody> deleteAuthorEntry(@Path("id") String id);

    @PUT("/authors/{id}")
    Call<AuthorApiModel> updateAuthorEntry(@Path("id") String id, @Body AuthorApiModel author);
}
