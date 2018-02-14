package com.example.abhishek.bookcatalogwithfragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment implements View.OnClickListener {
    OptionsFragmentInteractionListener listener;
    Button btnGenre, btnAuthor, btnBook;

    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OptionsFragmentInteractionListener){
            listener= (OptionsFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context.getClass().getSimpleName()+" must implement OptionsFragment.OptionsFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        btnGenre = (Button) view.findViewById(R.id.btnGenre);
        btnAuthor = (Button) view.findViewById(R.id.btnAuthor);
        btnBook = (Button) view.findViewById(R.id.btnBook);

        btnGenre.setOnClickListener(this);
        btnAuthor.setOnClickListener(this);
        btnBook.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGenre:
                listener.onGenreClicked(this);
                break;
            case R.id.btnAuthor:
                listener.onAuthorClicked(this);
                break;
            case R.id.btnBook:
                listener.onBookClicked(this);
                break;
        }
    }

    public interface OptionsFragmentInteractionListener{
        void onGenreClicked(Fragment fragment);
        void onAuthorClicked(Fragment fragment);
        void onBookClicked(Fragment fragment);
    }
}
