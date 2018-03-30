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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Adapters.BookAdapter;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.RealmModel.Book;
import com.example.abhishek.bookcatalogwithfragment.models.dummy.DummyBook;
import com.example.abhishek.bookcatalogwithfragment.models.dummy.DummyBookImageUrls;
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
    TextView tvHeader;
    private static boolean shownOnlyAsList = false;

    BookFabButtonClickListener bookFabButtonClickListener;
    BookListFragmentInteractionListener listener;

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;
    private boolean shouldReloadOnResume = false;
    boolean isBookLoaded = false;

    private static final String ACTION_BOOK_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.books.all.result.success";
    private static final String ACTION_BOOK_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.books.all.result.failure";
    private static final String KEY_BOOK = "books";

    private static final String ARGS_GENRE_ID = "genreId";
    private static final String ARGS_AUTHOR_ID = "authorId";

    String listType = null, filterId = null;
    ArrayList<DummyBook> dummyBooks;

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
                    for(int i=0 ; i<bookList.size() ; i++){
                        dummyBooks.add(new DummyBook(DummyBookImageUrls.getImageUrl(i), bookList.get(i)));
                    }
                    bookAdapter.setBookList(dummyBooks);
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

    public static BookListFragment newInstanceForGenre(String genreId) {
        shownOnlyAsList = true;
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_GENRE_ID, genreId);
        fragment.setArguments(args);

        return fragment;
    }

    public static BookListFragment newInstanceForAuthor(String authorId) {
        shownOnlyAsList = true;
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_AUTHOR_ID, authorId);
        fragment.setArguments(args);

        return fragment;
    }

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BookFabButtonClickListener && context instanceof BookListFragmentInteractionListener) {
            bookFabButtonClickListener = (BookFabButtonClickListener) context;
            listener = (BookListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.getClass().getSimpleName() + " must implement BookListFragment.BookFabButtonClickListener and BookListFragment.BookListFragmentInteractionListener both.");

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

        /*bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, this);
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);

        tvHeader = (TextView) v.findViewById(R.id.tvHeader);

        recyclerView = (RecyclerView) v.findViewById(R.id.book_recycler_view);
        bookList = new ArrayList<>();
        dummyBooks = new ArrayList<>();
       // bookAdapter = new BookAdapter(bookList, this, shownOnlyAsList);
        bookAdapter = new BookAdapter(dummyBooks, this, shownOnlyAsList);
        shownOnlyAsList = false;
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

        // check if args are save
        //if getArgs is null then load all books
        // else check arg type (genre/ author) and load

        if (getArguments() != null) {
            tvHeader.setVisibility(View.GONE);
            if (getArguments().containsKey(ARGS_AUTHOR_ID)) {
                listType = "author";
                filterId = getArguments().getString(ARGS_AUTHOR_ID);
            } else if (getArguments().containsKey(ARGS_GENRE_ID)) {
                listType = "genre";
                filterId = getArguments().getString(ARGS_GENRE_ID);
            }
        }

        fabAddBook.setVisibility(listType==null?View.VISIBLE:View.GONE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BOOK_LIST_API_SUCCESS);
        filter.addAction(ACTION_BOOK_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);
       // if (shouldReloadOnResume) {
            loadBooks();
        /*}
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
        Call<List<Book>> call;

       /* if("author".equalsIgnoreCase(listType)){
            call = bookService.getBooksByAuthorId(filterId);
        } else if("genre".equalsIgnoreCase(listType)){
            call = bookService.getBooksByGenreId(filterId);
        }else {
            call = bookService.getAllBooks();
        }*/

        call = "author".equalsIgnoreCase(listType)
                ?  bookService.getBooksByAuthorId(filterId)
                : "genre".equalsIgnoreCase(listType)
                ? bookService.getBooksByGenreId(filterId)
                : bookService.getAllBooks();
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
    public void onSelectBook(int position, ImageView ivBook) {
        shouldReloadOnResume = true;
       /* Book selectedBook = bookList.get(position);
        listener.onBookSelected(selectedBook);*/
        DummyBook selectedBook = dummyBooks.get(position);
        listener.onBookSelected(this, position, selectedBook, ivBook);
    }

    public interface BookListFragmentInteractionListener {
      //  void onBookSelected(Book book);
          void onBookSelected(BookListFragment bookListFragment, int position, DummyBook book, ImageView view);
    }

    public interface BookFabButtonClickListener {
        void onBookFabClick();
    }

}
