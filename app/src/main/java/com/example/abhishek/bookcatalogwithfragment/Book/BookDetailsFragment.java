package com.example.abhishek.bookcatalogwithfragment.Book;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.Model.Book;
import com.example.abhishek.bookcatalogwithfragment.Model.DummyBook;
import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.AuthorInterface;
import com.example.abhishek.bookcatalogwithfragment.Network.BookInterface;
import com.example.abhishek.bookcatalogwithfragment.Network.GenreInterface;
import com.example.abhishek.bookcatalogwithfragment.R;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {

    TextView tvBookName, tvBookLanguage, tvBookPublishDate, tvBookNoOfPages, tvBookId,
            tvGenreType, tvAuthoName, tvFindBookByGenre, tvFindBookByAuthor;
    FloatingActionButton btnEditBook, btnDeleteBook;
    ProgressDialog mProgressDialog;
    ImageView ivBookDetails;

    private static final String KEY_GENRE = "genre";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_SHOULD_RELOAD = "shouldReload";
    public static final String KEY_IS_AUTHOR_LOADED = "isAuthorLoaded";
    public static final String KEY_IS_GENRE_LOADED = "isGenreLoaded";
    private static final String ARGS_SELECTED_BOOK = "selectedBook";

    private boolean shouldReload = false;
    private boolean isGenreLoaded = false, isAuthorLoaded = false, isBookLoaded = false;

    private String dummyImgUrl;

  //  Book book;
    DummyBook book;
    Author author;
    Genre genre;

    GetAllBooksOfParticularGenre getAllBooksOfParticularGenre;
    GetAllBooksOfParticularAuthor getAllBooksOfParticularAuthor;
    BookDetailsEditButtonClickListener bookDetailsEditButtonClickListener;

    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    private static final String ACTION_AUTHOR_NAME_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.author.name.result.success";
    private static final String ACTION_AUTHOR_NAME_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.author.name.result.failure";

    private static final String ACTION_GENRE_TYPE_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.genre.type.result.success";
    private static final String ACTION_GENRE_TYPE_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.genre.type.result.failure";

    private static final String ACTION_RELOAD_BOOK_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.book.list.result.success";
    private static final String ACTION_RELOAD_BOOK_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.book.list.result.failure";

    private LocalBroadcastManager broadcastManager = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_AUTHOR_NAME_API_SUCCESS:
                    author = intent.getParcelableExtra(AUTHOR_KEY_FOR_BROADCASTRECEIVER);
                    if (author != null) {
                        tvAuthoName.setText("Author : "+author.getName());
                    } else {
                        tvAuthoName.setText("Author : Not found ! ");
                    }
                    isAuthorLoaded = true;
                    postLoad();
                    break;

                case ACTION_AUTHOR_NAME_API_FAILURE:
                    Toast.makeText(getActivity(), "Api Failure", Toast.LENGTH_SHORT).show();
                    isAuthorLoaded = true;
                    postLoad();
                    break;

                case ACTION_GENRE_TYPE_API_SUCCESS:
                    genre = intent.getParcelableExtra(GENRE_KEY_FOR_BROADCASTRECEIVER);
                    if (genre != null) {
                        tvGenreType.setText("Genre type : "+genre.getName());
                    } else {
                        tvGenreType.setText("Genre type : Not found !");
                    }

                    isGenreLoaded = true;
                    postLoad();
                    break;

                case ACTION_GENRE_TYPE_API_FAILURE:
                    Toast.makeText(getActivity(), "Api Failure", Toast.LENGTH_SHORT).show();
                    isGenreLoaded = true;
                    postLoad();
                    break;
                case ACTION_RELOAD_BOOK_LIST_API_SUCCESS:
                    Book extra = intent.getParcelableExtra(BOOK_KEY_FOR_BROADCASTRECEIVER);
                    book.setBook(extra);
                    book.setImageUrl(dummyImgUrl);
                    /*tvBookName.setText(book.getBook().getName());
                    tvBookId.setText(book.getBook().getId());
                    tvBookLanguage.setText(book.getBook().getLanguage());
                    tvBookPublishDate.setText(book.getBook().getPublished());
                    tvBookNoOfPages.setText(String.valueOf(book.getBook().getPages()));
                    isBookLoaded = true;
                    loadAuthorName(book.getBook().getAuthorId());
                    loadGenreType(book.getBook().getGenreId());*/
                    isBookLoaded = true;
                    setDataToViews(book);
                    break;
                case ACTION_RELOAD_BOOK_LIST_API_FAILURE:
                    Toast.makeText(getActivity(), "Api Failure", Toast.LENGTH_SHORT).show();
                    isBookLoaded = true;
                    postLoad();
                    break;

            }
        }
    };
    private static final String AUTHOR_KEY_FOR_BROADCASTRECEIVER = "author";
    private static final String GENRE_KEY_FOR_BROADCASTRECEIVER = "genre";
    private static final String BOOK_KEY_FOR_BROADCASTRECEIVER = "book";

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment getInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_SELECTED_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GetAllBooksOfParticularAuthor && context instanceof GetAllBooksOfParticularGenre && context instanceof BookDetailsEditButtonClickListener) {
            getAllBooksOfParticularAuthor = (GetAllBooksOfParticularAuthor) context;
            getAllBooksOfParticularGenre = (GetAllBooksOfParticularGenre) context;
            bookDetailsEditButtonClickListener = (BookDetailsEditButtonClickListener) context;
        } else {
            throw new RuntimeException(context.getClass().getSimpleName() + " must implement BookDetailsFragment.GetAllBooksOfParticularAuthor, BookDetailsFragment.BookDetailsEditButtonClickListener and BookDetailsFragment.GetAllBooksOfParticularGenre all.");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        /*if (savedInstanceState == null) {
            if (getArguments() == null)
                throw new RuntimeException("BookDetailsFragment must have arguments set. Are you calling BookDetailsFragment constructor directly? If so, consider using getInstance()");
            Bundle args = getArguments();

            if (!args.containsKey(ARGS_SELECTED_BOOK))
                throw new RuntimeException("BookDetailsFragment has arguments set, but arguments does not contain any selectedBook");
            book = args.getParcelable(ARGS_SELECTED_BOOK);
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        ivBookDetails = (ImageView) v.findViewById(R.id.ivBookDetails);
        tvBookName = (TextView) v.findViewById(R.id.tv_book_name);
        tvBookId = (TextView) v.findViewById(R.id.tv_book_id);
        tvBookLanguage = (TextView) v.findViewById(R.id.tv_book_language);
        tvBookPublishDate = (TextView) v.findViewById(R.id.tv_book_dateOfPublish);
        tvBookNoOfPages = (TextView) v.findViewById(R.id.tv_book_pages);
        tvGenreType = (TextView) v.findViewById(R.id.tv_book_genreType);
        tvAuthoName = (TextView) v.findViewById(R.id.tv_book_authorName);

        tvFindBookByAuthor = (TextView) v.findViewById(R.id.tv_find_all_books_by_author);
        tvFindBookByGenre = (TextView) v.findViewById(R.id.tv_find_all_by_genre_type);


        btnEditBook = (FloatingActionButton) v.findViewById(R.id.btn_book_edit);
        btnDeleteBook = (FloatingActionButton) v.findViewById(R.id.btn_book_delete);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        final Bundle b = getArguments();
        if (b != null) {
            String transitionName = b.getString("transitionName");
            book = (DummyBook) b.getSerializable("book");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivBookDetails.setTransitionName(transitionName);
            }

            if (book != null) {
                setDataToViews(book);
            }
        }


        tvFindBookByGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReload = false;
                if (genre != null)
                    getAllBooksOfParticularGenre.onParticularGenreSelected(genre);

            }
        });

        tvFindBookByAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReload = false;
                if (author != null)
                    getAllBooksOfParticularAuthor.onParticularAuthorSelected(author);
            }
        });

        btnDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> call = bookService.deleteBookEntry(book.getBook().getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Toast.makeText(BookActivity.this, "Sucessfully deleted entry",Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("#", t.toString());
                    }
                });

            }
        });

        btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReload = true;
                dummyImgUrl = book.getImageUrl();
                if (genre == null) {
                    bookDetailsEditButtonClickListener.onEditClicked(book, new Genre(), author);
                } else
                    bookDetailsEditButtonClickListener.onEditClicked(book, genre, author);
            }
        });
        return v;
    }

    private void setDataToViews(DummyBook book) {
        if (!TextUtils.isEmpty(book.getImageUrl()))
            Picasso.get().load(book.getImageUrl()).into(ivBookDetails);

        tvBookName.setText(book.getBook().getName());
        tvBookId.setText("Id : "+book.getBook().getId());
        tvBookLanguage.setText("Language : "+book.getBook().getLanguage());
        tvBookPublishDate.setText("Date of publish : "+book.getBook().getPublished());
        tvBookNoOfPages.setText("Pages : "+String.valueOf(book.getBook().getPages()));

        loadGenreType(book.getBook().getGenreId());
        loadAuthorName(book.getBook().getAuthorId());
    }

    public interface BookDetailsEditButtonClickListener {
        void onEditClicked(DummyBook book, Genre genre, Author author);
    }

    public interface GetAllBooksOfParticularGenre {
        void onParticularGenreSelected(Genre genre);
    }

    public interface GetAllBooksOfParticularAuthor {
        void onParticularAuthorSelected(Author author);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUTHOR_NAME_API_SUCCESS);
        filter.addAction(ACTION_AUTHOR_NAME_API_FAILURE);
        filter.addAction(ACTION_GENRE_TYPE_API_SUCCESS);
        filter.addAction(ACTION_GENRE_TYPE_API_FAILURE);
        filter.addAction(ACTION_RELOAD_BOOK_LIST_API_SUCCESS);
        filter.addAction(ACTION_RELOAD_BOOK_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        if (shouldReload) {
            reloadUpdatedBook(book.getBook().getId());
        }
        shouldReload = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void reloadUpdatedBook(String id) {
        isBookLoaded = false;
        showLoading();

        Call<Book> call = bookService.getBook(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book b = response.body();
                Intent intent = new Intent(ACTION_RELOAD_BOOK_LIST_API_SUCCESS);
                intent.putExtra(BOOK_KEY_FOR_BROADCASTRECEIVER, b);
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Intent intent = new Intent(ACTION_RELOAD_BOOK_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });

        shouldReload = false;
    }

    private void loadAuthorName(String authorId) {

        isAuthorLoaded = false;
        showLoading();

        Call<Author> call = authorService.getAuthor(authorId);
        call.enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                Author auth = response.body();
                Intent intent = new Intent(ACTION_AUTHOR_NAME_API_SUCCESS);
                intent.putExtra(AUTHOR_KEY_FOR_BROADCASTRECEIVER, auth);
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {
                Log.e("#", t.toString());
                Intent intent = new Intent(ACTION_AUTHOR_NAME_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });

    }

    private void loadGenreType(String genreId) {

        isGenreLoaded = false;
        showLoading();

        Call<Genre> call = genreService.getGenre(genreId);
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                Genre gen = response.body();
                Intent intent = new Intent(ACTION_GENRE_TYPE_API_SUCCESS);
                intent.putExtra(GENRE_KEY_FOR_BROADCASTRECEIVER, gen);
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                Log.e("#", t.toString());
                Intent intent = new Intent(ACTION_GENRE_TYPE_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });

    }

    private void postLoad() {
        if (isAuthorLoaded && isGenreLoaded)
            hideLoading();
    }

    private void showLoading() {
        if (mProgressDialog.isShowing())
            return;
        mProgressDialog.setMessage("Loading.......");
        mProgressDialog.show();
    }

    private void hideLoading() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


}
