package com.example.abhishek.bookcatalogwithfragment.Author;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.abhishek.bookcatalogwithfragment.Adapters.GenreAdapter;
import com.example.abhishek.bookcatalogwithfragment.Adapters.ListItemClickListener;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreListFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.AuthorInterface;
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
public class AuthorListFragment extends Fragment implements ListItemClickListener {

    private AuthorListFragmentInteractionListener listener;

    private RecyclerView recyclerView;
    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;
    private boolean shouldReloadOnResume = false;

    private static final String ACTION_AUTHOR_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.authors.all.result.success";
    private static final String ACTION_AUTHOR_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.authors.all.result.failure";
    private static final String KEY_AUTHORS = "authors";

    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";
    public static final String KEY_IS_AUTHOR_LOADED = "isAuthorLoaded";

    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    private boolean isAuthorLoaded = false;

    List<Author> authors = new ArrayList<>();
    AuthorAdapter adapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_AUTHOR_LIST_API_SUCCESS:
                    Toast.makeText(getActivity(), "Api Success", Toast.LENGTH_SHORT).show();
                    authors = Arrays.asList((Author[]) intent.getParcelableArrayExtra(KEY_AUTHORS));
                    adapter.setAuthorList(authors);
                    isAuthorLoaded = true;
                    postLoad();
                    break;


                case ACTION_AUTHOR_LIST_API_FAILURE:
                    Toast.makeText(getActivity(), "Api Failure", Toast.LENGTH_SHORT).show();
                    isAuthorLoaded = true;
                    postLoad();
                    break;
            }

        }
    };

    public AuthorListFragment() {
        // Required empty public constructor
    }
/*

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof GenreListFragment.GenreListFragmentInteractionListener){
            listener= (AuthorListFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context.getClass().getSimpleName()+" must implement AuthorListFragment.AuthorListFragmentInteractionListener");
        }
    }
*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        //set up the "loading...." dialog box
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        authors = new ArrayList<>();
        adapter=new AuthorAdapter(authors, this);

        loadAuthors();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_author_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.author_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUTHOR_LIST_API_SUCCESS);
        filter.addAction(ACTION_AUTHOR_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        /*if (shouldReloadOnResume) {
            loadAuthors();
        }
        shouldReloadOnResume = false;*/
    }

    @Override
    public void onAction(int position, int action) {

    }

    private void loadAuthors() {

        isAuthorLoaded = false;
        showLoading();

        Call<List<Author>> call = authorService.getAllAuthors();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                Intent intent = new Intent(ACTION_AUTHOR_LIST_API_SUCCESS);
                intent.putExtra(KEY_AUTHORS, response.body().toArray(new Author[0]));
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
              //  Log.e(TAG, t.toString());
                Intent intent = new Intent(ACTION_AUTHOR_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });

    }

    private void showLoading() {
        adapter.setLoading(true);
        if (mProgressDialog.isShowing())
            return;
        mProgressDialog.setMessage("Loading.......");
        mProgressDialog.show();
    }

    private void hideLoading() {
        adapter.setLoading(false);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void postLoad() {
        if (isAuthorLoaded)
            hideLoading();
    }

    private interface AuthorListFragmentInteractionListener{
        void onAuthorSelected();
    }
}
