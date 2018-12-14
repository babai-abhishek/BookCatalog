package com.example.abhishek.bookcatalogwithfragment.Network;

import com.example.abhishek.bookcatalogwithfragment.models.api.BookApiModel;

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

public interface BookInterface {

    @GET("/books")
    Call<List<BookApiModel>> getAllBooks();

    @GET("/books/{id}")
    Call<BookApiModel> getBook(@Path("id") String id);

    @GET("/books/genre/{genreId}")
    Call<List<BookApiModel>> getBooksByGenreId(@Path("genreId") String genreId);

    @GET("/books/author/{authorId}")
    Call<List<BookApiModel>> getBooksByAuthorId(@Path("authorId") String authorId);

    @POST("/books")
    Call<BookApiModel> newBookEntry(@Body BookApiModel book);

    @DELETE("/books/{id}")
    Call<ResponseBody> deleteBookEntry(@Path("id") String id);

    @PUT("/books/{id}")
    Call<BookApiModel> updateBookEntry(@Path("id") String id, @Body BookApiModel book);

}
