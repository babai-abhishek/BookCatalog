package com.example.abhishek.bookcatalogwithfragment.Book;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Adapters.GenreAdapter;
import com.example.abhishek.bookcatalogwithfragment.Adapters.SelectGenreAdapter;
import com.example.abhishek.bookcatalogwithfragment.Author.AuthorAddFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreAddFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreListFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.GenreInterface;
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
public class SelectGenreListFragment extends DialogFragment {

    private static final String TAG_FRAGMENT_GENRE_ADD ="GenreAddFragment";
    public static final int REQUEST_CODE_SELECT_GENRE = 2;
    private Genre selectedGenre;

    private static final String ACTION_GENRE_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.genres.all.result.success";
    private static final String ACTION_GENRE_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.genres.all.result.failure";
    private static final String KEY_GENRES = "genres";

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;
    private boolean shouldReloadOnResume = false;

    private RecyclerView rvSelectGenre;
    private FloatingActionButton fabAddGenre;

    List<Genre> genres = new ArrayList<>();
    SelectGenreAdapter adapter;

    private boolean isGenreLoaded = false;

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {

                //action taken on sucessful genre list loading
                case ACTION_GENRE_LIST_API_SUCCESS:
                    Toast.makeText(getActivity(), "Api Success", Toast.LENGTH_SHORT).show();
                    genres = Arrays.asList((Genre[]) intent.getParcelableArrayExtra(KEY_GENRES));
                    adapter.setGenreList(genres);
                    isGenreLoaded = true;
                    postLoad();
                    break;

                //action taken on un-sucessful genre list loading
                case ACTION_GENRE_LIST_API_FAILURE:
                    Toast.makeText(getActivity(), "Api Failure", Toast.LENGTH_SHORT).show();
                    isGenreLoaded = true;
                    postLoad();
                    break;

            }

        }
    };

    public SelectGenreListFragment() {
        // Required empty public constructor
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_genre_list, container, false);
        genres = new ArrayList<>();
        adapter = new SelectGenreAdapter(genres, new SelectGenreAdapter.GenreSelectionListener() {
            @Override
            public void onSelectGenre(Genre genre) {
                selectedGenre = genre;
                dispatchSelectedGenre();
            }
        });

        rvSelectGenre = (RecyclerView) v.findViewById(R.id.rv_select_genre);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSelectGenre.setLayoutManager(layoutManager);
        rvSelectGenre.setHasFixedSize(true);
        rvSelectGenre.setAdapter(adapter);

        loadGenres();

        FloatingActionButton fabAddGenre = (FloatingActionButton) v.findViewById(R.id.fab_add_genre);
        fabAddGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getChildFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction().addToBackStack(TAG_FRAGMENT_GENRE_ADD);

                final GenreAddFragment fragment = new GenreAddFragment();

                fragment.setTargetFragment(SelectGenreListFragment.this, BookAddFragment.REQUEST_CODE_ADD_GENRE);

                fragment.show(transaction, TAG_FRAGMENT_GENRE_ADD);

            }
        });


        return v;
    }

    private void dispatchSelectedGenre() {
        Intent i = new Intent();
        i.putExtra("genre", selectedGenre);

        getTargetFragment().onActivityResult(REQUEST_CODE_SELECT_GENRE, Activity.RESULT_OK, i);

        dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case BookAddFragment.REQUEST_CODE_ADD_GENRE:
                selectedGenre = (Genre) data.getParcelableExtra("genre");
                dispatchSelectedGenre();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //register broadcastreceiver  with Actions
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GENRE_LIST_API_SUCCESS);
        filter.addAction(ACTION_GENRE_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        if (shouldReloadOnResume) {
            loadGenres();
        }

        shouldReloadOnResume = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        //un-register broadcastreceiver
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void loadGenres() {

        isGenreLoaded = false;
        showLoading();

        Call<List<Genre>> call = genreService.getAllGenres();
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful()) { //if(response.code()>=200 && response.code()<300)
                    Intent intent = new Intent(ACTION_GENRE_LIST_API_SUCCESS);
                    intent.putExtra(KEY_GENRES, response.body().toArray(new Genre[0]));
                    broadcastManager.sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                // Log.e(TAG, t.toString());
                Intent intent = new Intent(ACTION_GENRE_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });
    }

    private void showLoading() {
        // adapter.setLoading(true);
        if (mProgressDialog.isShowing())
            return;
        mProgressDialog.setMessage("Loading.......");
        mProgressDialog.show();
    }

    private void hideLoading() {
        //adapter.setLoading(false);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void postLoad() {
        if (isGenreLoaded)
            hideLoading();
    }


}
