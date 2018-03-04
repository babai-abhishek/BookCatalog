package com.example.abhishek.bookcatalogwithfragment.Book;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Adapters.AuthorAdapter;
import com.example.abhishek.bookcatalogwithfragment.Adapters.SelectAuthorAdapter;
import com.example.abhishek.bookcatalogwithfragment.Author.AuthorAddFragment;
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
public class SelectAuthorListFragment extends DialogFragment {
    public static final int REQUEST_CODE_SELECT_AUTHOR = 1;

    private static final String TAG_FRAGMENT_AUTHOR_ADD = "AuthorAddFragment";

    RecyclerView rvSelectAuthor;
    List<Author> authors = new ArrayList<>();
    SelectAuthorAdapter adapter;
    public Author selectedAuthor;

    private boolean shouldReloadOnResume = false;

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;

    private static final String ACTION_AUTHOR_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.authors.all.result.success";
    private static final String ACTION_AUTHOR_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.authors.all.result.failure";
    private static final String KEY_AUTHORS = "authors";

    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    private boolean isAuthorLoaded = false;

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

    public SelectAuthorListFragment() {
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
        View v = inflater.inflate(R.layout.fragment_select_author_list, container, false);

        authors = new ArrayList<>();
        adapter = new SelectAuthorAdapter(authors, new SelectAuthorAdapter.AuthorSelectionListener() {
            @Override
            public void onSelectAuthor(Author author) {
                selectedAuthor = author;

                dispatchSelectedAuthor();
            }
        });
        rvSelectAuthor = (RecyclerView) v.findViewById(R.id.rv_select_author);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSelectAuthor.setLayoutManager(layoutManager);
        rvSelectAuthor.setHasFixedSize(true);
        rvSelectAuthor.setAdapter(adapter);

        loadAuthors();

        FloatingActionButton fabAddAuthor = (FloatingActionButton) v.findViewById(R.id.fab_add_author);
        fabAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getChildFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction().addToBackStack(TAG_FRAGMENT_AUTHOR_ADD);

                final AuthorAddFragment fragment = new AuthorAddFragment();

                fragment.setTargetFragment(SelectAuthorListFragment.this, BookAddFragment.REQUEST_CODE_ADD_AUTHOR);

                fragment.show(transaction, TAG_FRAGMENT_AUTHOR_ADD);

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case BookAddFragment.REQUEST_CODE_ADD_AUTHOR:
                selectedAuthor = (Author) data.getParcelableExtra("author");
                dispatchSelectedAuthor();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dispatchSelectedAuthor() {
        Intent i = new Intent();
        i.putExtra("author", selectedAuthor);

        getTargetFragment().onActivityResult(REQUEST_CODE_SELECT_AUTHOR, Activity.RESULT_OK, i);

        dismiss();
    }

//    @NonNull
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//
//        // Add back button listener
//        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
//                // getAction to make sure this doesn't double fire
//                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                    // Your code here
//                    dismiss();
//                    return true; // Capture onKey
//                }
//                return false; // Don't capture
//            }
//        });
//
//        return dialog;
//    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUTHOR_LIST_API_SUCCESS);
        filter.addAction(ACTION_AUTHOR_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);
       /* if (shouldReloadOnResume) {
            loadAuthors();
        }
        shouldReloadOnResume = false;*/
    }

    @Override
    public void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(broadcastReceiver);
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
        if (isAuthorLoaded)
            hideLoading();
    }

}
