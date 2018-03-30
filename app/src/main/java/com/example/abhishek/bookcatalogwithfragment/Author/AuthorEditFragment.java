package com.example.abhishek.bookcatalogwithfragment.Author;


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

import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.AuthorInterface;
import com.example.abhishek.bookcatalogwithfragment.R;
import com.example.abhishek.bookcatalogwithfragment.models.api.AuthorApiModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorEditFragment extends Fragment {

    EditText etEditAuthName, etEditAuthLang, etEditAuthCountry;
    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);

    private static final String ARGS_AUTHOR_NAME = "authorName";
    private static final String ARGS_AUTHOR_ID = "authorId";
    private static final String ARGS_AUTHOR_LANGUAGES = "authorlanguage";
    private static final String ARGS_AUTHOR_COUNTRY = "authorCountry";

    private String authName, authId, autLanguage, authCountry;


    public AuthorEditFragment() {
        // Required empty public constructor
    }

    public static AuthorEditFragment getInstance(String authName, String authId, String authLanguage, String authCountry){
        AuthorEditFragment fragment = new AuthorEditFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_AUTHOR_NAME, authName);
        args.putString(ARGS_AUTHOR_ID, authId);
        args.putString(ARGS_AUTHOR_LANGUAGES, authLanguage);
        args.putString(ARGS_AUTHOR_COUNTRY, authCountry);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            if(getArguments()==null)
                throw new RuntimeException("AuthorEditFragment must have arguments set. Are you calling AuthorEditFragment constructor directly? If so, consider using getInstance()");
            Bundle args = getArguments();

            if(!args.containsKey(ARGS_AUTHOR_ID))
                throw new RuntimeException("AuthorEditFragment has arguments set, but arguments does not contain any authId");
            else if(!args.containsKey(ARGS_AUTHOR_NAME))
                throw new RuntimeException("AuthorEditFragment has arguments set, but arguments does not contain any authName");
            else if(!args.containsKey(ARGS_AUTHOR_LANGUAGES))
                throw new RuntimeException("AuthorEditFragment has arguments set, but arguments does not contain any authLanguage");
            else if(!args.containsKey(ARGS_AUTHOR_COUNTRY))
                throw new RuntimeException("AuthorEditFragment has arguments set, but arguments does not contain any authCountry");

            authName = args.getString(ARGS_AUTHOR_NAME);
            authId = args.getString(ARGS_AUTHOR_ID);
            autLanguage = args.getString(ARGS_AUTHOR_LANGUAGES);
            authCountry = args.getString(ARGS_AUTHOR_COUNTRY);

        }else{

        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.author_edit_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.update_author:
                updateAuthor();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_edit_author, container, false);

        etEditAuthName = (EditText) v.findViewById(R.id.et_edit_author_name);
        etEditAuthLang = (EditText) v.findViewById(R.id.et_edit_author_language);
        etEditAuthCountry = (EditText) v.findViewById(R.id.et_edit_author_country);

        etEditAuthName.setText(authName);
        etEditAuthLang.setText(autLanguage);
        etEditAuthCountry.setText(authCountry);

        return v;
    }

    private void updateAuthor() {
        Call<AuthorApiModel> call = authorService.updateAuthorEntry
                (authId, new AuthorApiModel(null, etEditAuthName.getText().toString(), etEditAuthLang.getText().toString(), etEditAuthCountry.getText().toString()));
        call.enqueue(new Callback<AuthorApiModel>() {
            @Override
            public void onResponse(Call<AuthorApiModel> call, Response<AuthorApiModel> response) {
                //Log.e(TAG,response.body().getName());
                Toast.makeText(getActivity(), "Sucessfully updated with new name "+response.body().getName(),Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<AuthorApiModel> call, Throwable t) {
                Log.e("#",t.toString());
            }
        });
    }
}
