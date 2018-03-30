package com.example.abhishek.bookcatalogwithfragment.Author;


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

import com.example.abhishek.bookcatalogwithfragment.Adapters.AuthorAdapter;
import com.example.abhishek.bookcatalogwithfragment.Adapters.ListItemClickListener;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.AuthorInterface;
import com.example.abhishek.bookcatalogwithfragment.R;
import com.example.abhishek.bookcatalogwithfragment.dao.AuthorDao;
import com.example.abhishek.bookcatalogwithfragment.models.api.AuthorApiModel;
import com.example.abhishek.bookcatalogwithfragment.models.bl.AuthorBusinessModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorListFragment extends DialogFragment implements ListItemClickListener, AuthorAdapter.SelectFromDialog {

    private AuthorListFragmentInteractionListener listener;
    private AuthorFabButtonClickListener authorFabButtonClickListener;
    boolean shownAsDialog = false;
    private AuthorBusinessModel selectedAuthor;
    private RecyclerView recyclerView;
    FloatingActionButton fabAddAuthor;

    public static final int REQUEST_CODE_SELECT_AUTHOR = 1;
    public static final int REQUEST_CODE_ADD_AUTHOR = 0;

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;
    private boolean shouldReloadOnResume = false;

    private static final String ACTION_AUTHOR_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.authors.all.result.success";
    private static final String ACTION_AUTHOR_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.authors.all.result.failure";
    private static final String KEY_AUTHORS = "authors";
    private static final String TAG_FRAGMENT_AUTHOR_ADD ="AuthorAddFragment";

    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";
    public static final String KEY_IS_AUTHOR_LOADED = "isAuthorLoaded";

//    public static final int REQUEST_CODE_ADD_AUTHOR = 0;


    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    private boolean isAuthorLoaded = false;

    List<AuthorBusinessModel> authors = new ArrayList<>();
    AuthorAdapter adapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_AUTHOR_LIST_API_SUCCESS:
                    Toast.makeText(getActivity(), "Api Success", Toast.LENGTH_SHORT).show();
                    authors = Arrays.asList((AuthorBusinessModel[]) intent.getParcelableArrayExtra(KEY_AUTHORS));
                    AuthorDao.save(authors);
                    authors = AuthorDao.getAll();
                    if(!shownAsDialog){
                        adapter.setAuthorList(authors);
                    }else {
                        adapter.setAuthorListForDialog(authors);
                    }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof AuthorListFragmentInteractionListener && context instanceof AuthorFabButtonClickListener){

            listener= (AuthorListFragmentInteractionListener) context;
            authorFabButtonClickListener = (AuthorFabButtonClickListener) context;

        } else{
            throw new RuntimeException(context.getClass().getSimpleName()+" must implement AuthorListFragment.AuthorListFragmentInteractionListener and AuthorFabButtonClickListener both.");
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

        authors = new ArrayList<>();
        if(shownAsDialog){
            adapter = new AuthorAdapter(authors, (AuthorAdapter.SelectFromDialog) this);
        }else {
            adapter = new AuthorAdapter(authors, (ListItemClickListener) this);
        }

        loadAuthors();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_author_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.author_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fabAddAuthor = (FloatingActionButton) v.findViewById(R.id.fab_add_author);
        fabAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shownAsDialog){
                    shouldReloadOnResume = true;
                    authorFabButtonClickListener.onAuthorFabClick();
                }else {
                    FragmentManager manager = getChildFragmentManager();

                    FragmentTransaction transaction = manager.beginTransaction().addToBackStack(TAG_FRAGMENT_AUTHOR_ADD);

                    final AuthorAddFragment fragment = new AuthorAddFragment();

                    fragment.setTargetFragment(AuthorListFragment.this, REQUEST_CODE_ADD_AUTHOR);

                    fragment.show(transaction, TAG_FRAGMENT_AUTHOR_ADD);
                }
            }
        });
     //   getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return v;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int ret = super.show(transaction, tag);

        shownAsDialog = true;

        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUTHOR_LIST_API_SUCCESS);
        filter.addAction(ACTION_AUTHOR_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        if (shouldReloadOnResume) {
            loadAuthors();
        }
        shouldReloadOnResume = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onAction(int position, int action) {
        AuthorBusinessModel author = authors.get(position);

        switch (action) {

            case AuthorAdapter.ACTION_EDIT:
                shouldReloadOnResume = true;
                listener.onAuthorSelected(author.getName(), author.getId(), author.getLanguage(),
                        author.getCountry());
                break;

            case AuthorAdapter.ACTION_DELETE:
                Call<ResponseBody> call = authorService.deleteAuthorEntry(author.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //  Toast.makeText(AuthorActivity.this, "Sucessfully deleted entry",Toast.LENGTH_SHORT).show();
                        loadAuthors();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Log.e(TAG, t.toString());
                    }
                });
                break;
        }
    }

    private void loadAuthors() {

        isAuthorLoaded = false;
        showLoading();

        Call<List<AuthorApiModel>> call = authorService.getAllAuthors();
        call.enqueue(new Callback<List<AuthorApiModel>>() {
            @Override
            public void onResponse(Call<List<AuthorApiModel>> call, Response<List<AuthorApiModel>> response) {
                if(response.isSuccessful()) {
                    List<AuthorBusinessModel> authorBusinessModels = new ArrayList<>();
                    for (AuthorApiModel apiModel : response.body()) {
                        authorBusinessModels.add(new AuthorBusinessModel(apiModel));
                    }
                    Intent intent = new Intent(ACTION_AUTHOR_LIST_API_SUCCESS);
                    intent.putExtra(KEY_AUTHORS,authorBusinessModels.toArray(new AuthorBusinessModel[0]));
                    broadcastManager.sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<List<AuthorApiModel>> call, Throwable t) {
                //  Log.e(TAG, t.toString());
                Intent intent = new Intent(ACTION_AUTHOR_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });

    }

    private void dispatchSelectedAuthor() {
        Intent i = new Intent();
        i.putExtra("author", selectedAuthor);
        getTargetFragment().onActivityResult(REQUEST_CODE_SELECT_AUTHOR, Activity.RESULT_OK, i);
        dismiss();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_ADD_AUTHOR:
                selectedAuthor = (AuthorBusinessModel) data.getParcelableExtra("author");
                dispatchSelectedAuthor();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSelect(AuthorBusinessModel author) {
        selectedAuthor = author;
        dispatchSelectedAuthor();
    }

    public interface AuthorListFragmentInteractionListener {
        void onAuthorSelected(String authName, String authId, String authLanguage, String authCountry);
    }

    public interface AuthorFabButtonClickListener{
        public void onAuthorFabClick();
    }

}
