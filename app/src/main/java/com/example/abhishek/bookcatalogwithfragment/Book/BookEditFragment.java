package com.example.abhishek.bookcatalogwithfragment.Book;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.Model.Book;
import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.BookInterface;
import com.example.abhishek.bookcatalogwithfragment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookEditFragment extends Fragment {

    EditText etBookName, etBookLang, etBookPublishDate, etBookPages, etBookGenre, etBookAuthor;
    Button btnSaveBook, btnChangeAuthor, btnChangeGenre;

    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);

    Author selectedAuthor = new Author();
    Genre selectedGenre = new Genre();
    Book selectedBook = new Book();

    public BookEditFragment() {
        // Required empty public constructor
    }

    public static BookEditFragment newInstance(Book book, Genre genre, Author author){
        BookEditFragment fragment = new BookEditFragment();
        Bundle args = new Bundle();
        args.putParcelable("genre",genre);
        args.putParcelable("author",author);
        args.putParcelable("book",book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            if(getArguments() == null)
                throw new RuntimeException("BookEditFragment must have arguments set. Are you calling BookEditFragment constructor directly? If so, consider using getInstance()");
            Bundle args = getArguments();

            if(!args.containsKey("genre"))
                throw new RuntimeException("BookDetailsFragment has arguments set, but arguments does not contain any genre");
            selectedGenre = args.getParcelable("genre");

            if(!args.containsKey("book"))
                throw new RuntimeException("BookDetailsFragment has arguments set, but arguments does not contain any book");
            selectedBook = args.getParcelable("book");

            if(!args.containsKey("author"))
                throw new RuntimeException("BookDetailsFragment has arguments set, but arguments does not contain any author");
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

        etBookName.setText(selectedBook.getName());
        etBookLang.setText(selectedBook.getLanguage());
        etBookPublishDate.setText(selectedBook.getPublished());
        etBookPages.setText(String.valueOf(selectedBook.getPages()));
        setAuthor(selectedAuthor.getName());
        setGenre(selectedGenre.getName());


        return v;
    }

    private void setAuthor(String authorName) {
        etBookAuthor.setText(authorName);
    }

    private void setGenre(String name) {
        etBookGenre.setText(name);
    }

}
