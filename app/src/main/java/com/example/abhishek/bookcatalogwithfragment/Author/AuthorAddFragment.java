package com.example.abhishek.bookcatalogwithfragment.Author;


import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Book.BookAddFragment;
import com.example.abhishek.bookcatalogwithfragment.Book.BookListFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreListFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.AuthorInterface;
import com.example.abhishek.bookcatalogwithfragment.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorAddFragment extends DialogFragment {

    EditText et_new_auth_name, et_new_auth_language, et_new_auth_country;
    Button btnSaveNewAuthor;
    boolean shownAsDialog = false;
    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);

    public AuthorAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setHasOptionsMenu(true);
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.author_add_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_author:
                addNewAuthor();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_author_add, container, false);

        et_new_auth_name = (EditText) v.findViewById(R.id.et_add_new_author_name);
        et_new_auth_language = (EditText) v.findViewById(R.id.et_add_new_author_language);
        et_new_auth_country = (EditText) v.findViewById(R.id.et_add_new_author_country);

        btnSaveNewAuthor = (Button) v.findViewById(R.id.btn_save_new_author);
        btnSaveNewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAuthor();
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

    private void addNewAuthor() {

        Call<Author> call = authorService.newAuthorEntry(new Author(et_new_auth_name.getText().toString(),
                et_new_auth_language.getText().toString(),
                et_new_auth_country.getText().toString()));

        call.enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                Author author = response.body();
                //Log.d("#"," id of new book received "+ author.getId());
                Toast.makeText(getActivity(), "ID of new author is"+author.getId(), Toast.LENGTH_SHORT).show();
               // getActivity().onBackPressed();
                if(!shownAsDialog){
                    getActivity().onBackPressed();
                }
                else {
                    Intent i = new Intent();
                    i.putExtra("author", author);
                    getTargetFragment().onActivityResult(BookAddFragment.REQUEST_CODE_ADD_AUTHOR, Activity.RESULT_OK, i);
                    dismiss();
                }

            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {
                Log.e("#",t.toString());
            }
        });
    }

}
