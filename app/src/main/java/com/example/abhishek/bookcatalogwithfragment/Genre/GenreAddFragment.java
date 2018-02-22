package com.example.abhishek.bookcatalogwithfragment.Genre;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.GenreInterface;
import com.example.abhishek.bookcatalogwithfragment.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenreAddFragment extends Fragment {

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);
    EditText et_add_new_genre;

    public GenreAddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.genre_add_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_genre:
                addNewGenre();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_genre_add, container, false);

        et_add_new_genre = (EditText) v.findViewById(R.id.et_add_new_genre);

        return v;
    }

    public void addNewGenre(){
        Call<Genre> call = genreService.newGenreEntry(new Genre(et_add_new_genre.getText().toString()));
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                Genre genre = response.body();
                Toast.makeText(getActivity(), "ID of new genre is"+genre.getId(), Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(AddNewGenreActivity.this,GenreActivity.class));
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                //Log.e(TAG,t.toString());
            }
        });

    }

}
