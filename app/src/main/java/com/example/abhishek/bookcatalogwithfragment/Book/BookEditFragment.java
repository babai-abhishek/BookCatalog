package com.example.abhishek.bookcatalogwithfragment.Book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Author.AuthorListFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreListFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.Model.Book;
import com.example.abhishek.bookcatalogwithfragment.Model.DummyBook;
import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.BookInterface;
import com.example.abhishek.bookcatalogwithfragment.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookEditFragment extends Fragment {

    EditText etBookName, etBookLang, etBookPublishDate, etBookPages, etBookGenre, etBookAuthor;
    Button btnSaveBook, btnChangeAuthor, btnChangeGenre;

    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);

    Author selectedAuthor = new Author();
    Genre selectedGenre = new Genre();
    //Book selectedBook = new Book();
    DummyBook selectedBook = new DummyBook();

    public BookEditFragment() {
        // Required empty public constructor
    }

    public static BookEditFragment newInstance(DummyBook book, Genre genre, Author author) {
        BookEditFragment fragment = new BookEditFragment();
        Bundle args = new Bundle();
        args.putParcelable("genre", genre);
        args.putParcelable("author", author);
        args.putParcelable("book", book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (getArguments() == null)
                throw new RuntimeException("BookEditFragment must have arguments set. Are you calling BookEditFragment constructor directly? If so, consider using getInstance()");
            Bundle args = getArguments();

            if (!args.containsKey("genre"))
                throw new RuntimeException("BookEditFragment has arguments set, but arguments does not contain any genre");
            selectedGenre = args.getParcelable("genre");

            if (!args.containsKey("book"))
                throw new RuntimeException("BookEditFragment has arguments set, but arguments does not contain any book");
            selectedBook = args.getParcelable("book");

            if (!args.containsKey("author"))
                throw new RuntimeException("BookEditFragment has arguments set, but arguments does not contain any author");
            selectedAuthor = args.getParcelable("author");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_edit, container, false);

        etBookName = (EditText) v.findViewById(R.id.et_edit_book_name);
        etBookLang = (EditText) v.findViewById(R.id.et_edit_book_language);
        etBookPublishDate = (EditText) v.findViewById(R.id.et_edit_book_publishdate);
        etBookPages = (EditText) v.findViewById(R.id.et_edit_book_pages);
        etBookAuthor = (EditText) v.findViewById(R.id.et_edit_book_author);
        etBookGenre = (EditText) v.findViewById(R.id.et_edit_book_genre);

        btnSaveBook = (Button) v.findViewById(R.id.btn_edit_book);
        btnChangeAuthor = (Button) v.findViewById(R.id.btn_change_author);
        btnChangeGenre = (Button) v.findViewById(R.id.btn_change_genre);

        etBookName.setText(selectedBook.getBook().getName());
        etBookLang.setText(selectedBook.getBook().getLanguage());
        etBookPublishDate.setText(selectedBook.getBook().getPublished());
        etBookPages.setText(String.valueOf(selectedBook.getBook().getPages()));
        setAuthor(selectedAuthor.getName());
        setGenre(selectedGenre.getName());

        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });

        btnChangeAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction().addToBackStack("SelectAuthor");
                final AuthorListFragment fragment = new AuthorListFragment();
                fragment.setTargetFragment(BookEditFragment.this, AuthorListFragment.REQUEST_CODE_SELECT_AUTHOR);
                fragment.show(transaction, "SelectAuthor");
            }
        });

        btnChangeGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction().addToBackStack("SelectGenre");
                final GenreListFragment fragment = new GenreListFragment();
                fragment.setTargetFragment(BookEditFragment.this, GenreListFragment.REQUEST_CODE_SELECT_GENRE);
                fragment.show(transaction, "SelectGenre");
            }
        });

        return v;
    }

    private void updateBook() {

        Call<Book> call = bookService.updateBookEntry(selectedBook.getBook().getId(),
                new Book(etBookName.getText().toString(),
                        etBookLang.getText().toString(),
                        etBookPublishDate.getText().toString(),
                        Integer.parseInt(etBookPages.getText().toString()),
                        selectedAuthor.getId(),
                        selectedGenre.getId()));
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                //  Log.e(TAG,response.body().getName());
                Toast.makeText(getActivity(), "Sucessfully updated with new name " + response.body().getName(), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("#", t.toString());
            }
        });

    }

    private void setAuthor(String authorName) {
        etBookAuthor.setText(authorName);
    }

    private void setGenre(String name) {
        etBookGenre.setText(name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case AuthorListFragment.REQUEST_CODE_SELECT_AUTHOR:
                selectedAuthor = (Author) data.getParcelableExtra("author");
                setAuthor(selectedAuthor.getName());
                break;

            case GenreListFragment.REQUEST_CODE_SELECT_GENRE:
                selectedGenre = data.getParcelableExtra("genre");
                setGenre(selectedGenre.getName());
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
