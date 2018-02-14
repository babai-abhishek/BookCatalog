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
public class GenreDetailsFragment extends Fragment {
    private static final String ARGS_GENRE_ID = "genreId";

    private String genreId;

    public static GenreDetailsFragment getInstance(String genreId){
        GenreDetailsFragment fragment = new GenreDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_GENRE_ID, genreId);
        fragment.setArguments(args);
        return  fragment;
    }

    public GenreDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            if(getArguments()==null)
                throw new RuntimeException("GenreDetailsFragment must have arguments set. Are you calling GenreDetailsFragment constructor directly? If so, consider using getInstance()");
            Bundle args = getArguments();

            if(!args.containsKey(ARGS_GENRE_ID))
                throw new RuntimeException("GenreDetailsFragment has arguments set, but arguments does not contain any genreId");

            genreId=args.getString(ARGS_GENRE_ID);
        }else{

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_genre_details, container, false);
        TextView tvGenreId = (TextView) v.findViewById(R.id.tvGenreId);
        tvGenreId.setText(String.format("You have selected %s as genre id", genreId));
        return v;
    }

}
