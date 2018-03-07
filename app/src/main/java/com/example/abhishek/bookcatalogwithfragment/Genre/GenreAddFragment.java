package com.example.abhishek.bookcatalogwithfragment.Genre;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Book.BookAddFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.GenreInterface;
import com.example.abhishek.bookcatalogwithfragment.R;
import com.example.abhishek.bookcatalogwithfragment.Util.KeyBoardManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenreAddFragment extends DialogFragment {

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);
    EditText et_add_new_genre;
    Button btnSaveNewGenre;
    boolean shownAsDialog = false;

    public GenreAddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_genre_add, container, false);

        et_add_new_genre = (EditText) v.findViewById(R.id.et_add_new_genre);
        btnSaveNewGenre = (Button) v.findViewById(R.id.btn_save_new_genre);
        btnSaveNewGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewGenre();
                new KeyBoardManager().cancelkeyBoard(getActivity());
            }
        });

        return v;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setCanceledOnTouchOutside(false);
        return d;
    }


    @Override
    public int show(FragmentTransaction transaction, String tag) {

        int ret = super.show(transaction, tag);

        shownAsDialog = true;

        return ret;
    }

    public void addNewGenre(){
        Call<Genre> call = genreService.newGenreEntry(new Genre(et_add_new_genre.getText().toString()));
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                Genre genre = response.body();
                Toast.makeText(getActivity(), "ID of new genre is"+genre.getId(), Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(AddNewGenreActivity.this,GenreActivity.class));
               // getActivity().onBackPressed();

                if(!shownAsDialog){
                    getActivity().onBackPressed();
                }
                else {
                    Intent i = new Intent();
                    i.putExtra("genre", genre);
                    getTargetFragment().onActivityResult(GenreListFragment.REQUEST_CODE_ADD_GENRE, Activity.RESULT_OK, i);
                    dismiss();
                }

            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                //Log.e(TAG,t.toString());
            }
        });

    }

}
