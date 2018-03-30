package com.example.abhishek.bookcatalogwithfragment.Book;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Author.AuthorListFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreListFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.RealmModel.Author;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.RealmModel.Book;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.RealmModel.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.BookInterface;
import com.example.abhishek.bookcatalogwithfragment.R;
import com.example.abhishek.bookcatalogwithfragment.Util.KeyBoardManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookAddFragment extends Fragment {

    EditText etNewBookName, etNewBookLanguage, etNewBookPublishDate, etNewBookPages;

    final Calendar myCalendar = Calendar.getInstance();
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    Genre selectedGenre = new Genre();
    Author selectedAuthor = new Author();
    TextView tvGenreType, tvAuthorName;
    public static final int REQUEST_CODE_ADD_AUTHOR = 0;


    public BookAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_add_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_book:
                new KeyBoardManager().cancelkeyBoard(getActivity());
                addNewBook();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_add, container, false);

        tvGenreType = (TextView) v.findViewById(R.id.tv_select_genre_type);
        tvAuthorName = (TextView) v.findViewById(R.id.tv_select_author_name);

        setGenre();
        setAuthor();

        etNewBookName = (EditText) v.findViewById(R.id.et_new_book_name);
        etNewBookLanguage = (EditText) v.findViewById(R.id.et_new_book_language);
        etNewBookPages = (EditText) v.findViewById(R.id.et_new_book_pages);

        etNewBookPublishDate = (EditText) v.findViewById(R.id.et_new_book_publishDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etNewBookPublishDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tvAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the authors in a list and select from there
                FragmentManager manager = getChildFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction().addToBackStack("SelectAuthor");

                final AuthorListFragment fragment = new AuthorListFragment();

                fragment.setTargetFragment(BookAddFragment.this, AuthorListFragment.REQUEST_CODE_SELECT_AUTHOR);

                fragment.show(transaction, "SelectAuthor");

//                fragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        if(fragment.selectedAuthor!=null){
//
//                        }
//                    }
//                });

            }
        });

        tvGenreType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the genres in a list and select from there
                FragmentManager manager = getChildFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction().addToBackStack("SelectGenre");

                final GenreListFragment fragment = new GenreListFragment();

                fragment.setTargetFragment(BookAddFragment.this, GenreListFragment.REQUEST_CODE_SELECT_GENRE);

                fragment.show(transaction, "SelectGenre");

            }
        });

        return v;
    }

    private void addNewBook() {
        String bookName = etNewBookName.getText().toString().trim();
        String lang = etNewBookLanguage.getText().toString().trim();
        String date = etNewBookPublishDate.getText().toString();
        int pages = Integer.parseInt(String.valueOf(etNewBookPages.getText().toString().equals("") ? 0 : etNewBookPages.getText().toString()));
                /* String authId =  selectedAuthor.getId();
                 String genreId = selectedGenre.getId();*/

        if (bookName.isEmpty() || lang.isEmpty() || date.isEmpty() || pages <= 0 ||
                selectedAuthor.getId() == null || selectedGenre.getId() == null) {
            Toast.makeText(getActivity(), "Please fill all the fields correctly ", Toast.LENGTH_SHORT).show();
        } else {
            createNewBookEntry(new Book(bookName, lang,
                    date, pages,
                    selectedAuthor.getId(), selectedGenre.getId()));
        }

    }

    private void createNewBookEntry(Book book) {
        Call<Book> call = bookService.newBookEntry(book);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                Log.d("#", " id of new book received " + book.getId());
                Toast.makeText(getActivity(), "ID of new books is" + book.getId(), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("#", t.toString());
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etNewBookPublishDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void setGenre() {

        if (selectedGenre.getName() == null) {
            tvGenreType.setText("Click to select genre");
        } else {
            tvGenreType.setText("Selected Genre : " + selectedGenre.getName());
        }
    }

    private void setAuthor() {

        if (selectedAuthor.getName() == null) {
            tvAuthorName.setText("Click to select author");
        } else {
           // Log.d("#",selectedAuthor.getName());
            tvAuthorName.setText("Selected Author : " + selectedAuthor.getName());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case AuthorListFragment.REQUEST_CODE_SELECT_AUTHOR:
                selectedAuthor = (Author) data.getParcelableExtra("author");
                setAuthor();
                break;

            case GenreListFragment.REQUEST_CODE_SELECT_GENRE:
                selectedGenre = data.getParcelableExtra("genre");
                setGenre();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
