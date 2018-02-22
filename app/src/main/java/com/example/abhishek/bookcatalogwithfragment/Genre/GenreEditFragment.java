package com.example.abhishek.bookcatalogwithfragment.Genre;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
public class GenreEditFragment extends Fragment {

    private static final String ARGS_GENRE_NAME = "genreName";
    private static final String ARGS_GENRE_ID = "genreId";
    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    private String genreName, genreID;
    EditText etGenreName;

    public static GenreEditFragment getInstance(String genreName, String genreId){
        GenreEditFragment fragment = new GenreEditFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_GENRE_NAME, genreName);
        args.putString(ARGS_GENRE_ID, genreId);
        fragment.setArguments(args);
        return  fragment;
    }

    public GenreEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            if(getArguments()==null)
                throw new RuntimeException("GenreEditFragment must have arguments set. Are you calling GenreEditFragment constructor directly? If so, consider using getInstance()");
            Bundle args = getArguments();

            if(!args.containsKey(ARGS_GENRE_NAME))
                throw new RuntimeException("GenreEditFragment has arguments set, but arguments does not contain any genreId");

            genreName = args.getString(ARGS_GENRE_NAME);
            genreID = args.getString(ARGS_GENRE_ID);

        }else{

        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.genre_edit_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.update_genre:
                updateGenre();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_genre_edit, container, false);

        etGenreName = (EditText) v.findViewById(R.id.et_EditGenreType);
        etGenreName.setText(String.format(genreName));

       /* Button btnUpdateGenre = (Button) v.findViewById(R.id.btn_SaveEditGenre);
        btnUpdateGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGenre();
            }
        });*/
        return v;
    }

    public void updateGenre(){

        Call<Genre> call = genreService.updateGenreEntry(genreID,new Genre(etGenreName.getText().toString()));
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                Toast.makeText(getActivity(), "Sucessfully updated with new name "+response.body().getName(),Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                // Log.e(TAG,t.toString());
            }
        });
    }

}
