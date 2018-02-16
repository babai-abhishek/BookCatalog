package com.example.abhishek.bookcatalogwithfragment.Genre;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.bookcatalogwithfragment.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenreEditFragment extends Fragment {
    private static final String ARGS_GENRE_NAME = "genreName";

    private String genreName;

    public static GenreEditFragment getInstance(String genreId){
        GenreEditFragment fragment = new GenreEditFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_GENRE_NAME, genreId);
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

            genreName=args.getString(ARGS_GENRE_NAME);
        }else{

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_genre_details, container, false);
        TextView tvGenreId = (TextView) v.findViewById(R.id.tvGenreId);
        tvGenreId.setText(String.format("You have selected %s as genre id", genreName));
        return v;
    }

}
