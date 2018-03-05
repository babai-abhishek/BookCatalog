package com.example.abhishek.bookcatalogwithfragment.Book;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Adapters.AuthorAdapter;
import com.example.abhishek.bookcatalogwithfragment.Adapters.BookAdapter;
import com.example.abhishek.bookcatalogwithfragment.Model.Book;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.BookInterface;
import com.example.abhishek.bookcatalogwithfragment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment implements BookAdapter.SelectBookFromListListener {

    private RecyclerView recyclerView;
    FloatingActionButton fabAddBook;

    BookFabButtonClickListener bookFabButtonClickListener;
    BookListFragmentInteractionListener listener;

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;
    private boolean shouldReloadOnResume = false;
    boolean isBookLoaded = false;

    private static final String ACTION_BOOK_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.books.all.result.success";
    private static final String ACTION_BOOK_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.books.all.result.failure";
    private static final String KEY_BOOK = "books";

    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    private List<Book> bookList;
    private BookAdapter bookAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_BOOK_LIST_API_SUCCESS:
                    Toast.makeText(getActivity(), "Api Success", Toast.LENGTH_SHORT).show();
                    bookList = Arrays.asList((Book[]) intent.getParcelableArrayExtra(KEY_BOOK));
                    bookAdapter.setBookList(bookList);
                    isBookLoaded = true;
                    postLoad();
                    break;

                case ACTION_BOOK_LIST_API_FAILURE:
                    Toast.makeText(getActivity(), "Api Failure", Toast.LENGTH_SHORT).show();
                    isBookLoaded = true;
                    postLoad();
                    break;
            }
        }
    };

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof BookFabButtonClickListener && context instanceof BookListFragmentInteractionListener){
            bookFabButtonClickListener = (BookFabButtonClickListener) context;
            listener = (BookListFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.getClass().getSimpleName()+" must implement BookListFragment.BookFabButtonClickListener and BookListFragment.BookListFragmentInteractionListener both.");

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        //set up the "loading...." dialog box
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList,this);

        loadBooks();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.book_recycler_view);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookAdapter);

        fabAddBook = (FloatingActionButton) v.findViewById(R.id.fab_add_book);
        fabAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReloadOnResume = true;
                bookFabButtonClickListener.onBookFabClick();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BOOK_LIST_API_SUCCESS);
        filter.addAction(ACTION_BOOK_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);
/*
        if (shouldReloadOnResume) {
            loadBooks();
        }
        shouldReloadOnResume = false;*/
    }

    @Override
    public void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


    private void loadBooks() {

        isBookLoaded = false;
        showLoading();

        Call<List<Book>> call = bookService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                Intent intent = new Intent(ACTION_BOOK_LIST_API_SUCCESS);
                intent.putExtra(KEY_BOOK, response.body().toArray(new Book[0]));
                broadcastManager.sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Intent intent = new Intent(ACTION_BOOK_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });
    }

    private void showLoading() {
        bookAdapter.setLoading(true);
        if (mProgressDialog.isShowing())
            return;
        mProgressDialog.setMessage("Loading.......");
        mProgressDialog.show();
    }

    private void hideLoading() {
        bookAdapter.setLoading(false);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void postLoad() {
        if (isBookLoaded)
            hideLoading();
    }

    @Override
    public void onSelectBook(int position) {
        Book selectedBook = bookList.get(position);
        listener.onBookSelected(selectedBook);
    }

    public interface BookListFragmentInteractionListener{
        void onBookSelected(Book book);
    }

    public interface BookFabButtonClickListener{
        void onBookFabClick();
    }

}
