package com.example.abhishek.bookcatalogwithfragment.Genre;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.bookcatalogwithfragment.Adapters.GenreAdapter;
import com.example.abhishek.bookcatalogwithfragment.Adapters.ListItemClickListener;
import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.Network.ApiClient;
import com.example.abhishek.bookcatalogwithfragment.Network.GenreInterface;
import com.example.abhishek.bookcatalogwithfragment.R;

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
public class GenreListFragment extends Fragment implements ListItemClickListener{

    private GenreListFragmentInteractionListener listener;

    private static final String ACTION_GENRE_LIST_API_SUCCESS = "com.example.abhishek.bookcatalogwithfragment.api.genres.all.result.success";
    private static final String ACTION_GENRE_LIST_API_FAILURE = "com.example.abhishek.bookcatalogwithfragment.api.genres.all.result.failure";
    private static final String KEY_GENRES = "genres";

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;
    private boolean shouldReloadOnResume = false;

    private RecyclerView recyclerView;

    List<Genre> genres = new ArrayList<>();
    GenreAdapter adapter;

    private boolean isGenreLoaded = false;

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){

                //action taken on sucessful genre list loading
                case ACTION_GENRE_LIST_API_SUCCESS:
                    Toast.makeText(getActivity(), "Api Success", Toast.LENGTH_SHORT).show();
                    genres= Arrays.asList((Genre[])intent.getParcelableArrayExtra(KEY_GENRES));
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

    public GenreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof GenreListFragmentInteractionListener){
            listener= (GenreListFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context.getClass().getSimpleName()+" must implement GenreList.GenreListFragmentInteractionListener");
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

        genres = new ArrayList<>();
        adapter=new GenreAdapter(genres, this);

        loadGenres();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_genre_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.genre_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return v;
    }



    public  interface GenreListFragmentInteractionListener{
        void onGenreSelected(String genreName, String genreId);
    }


   @Override
   public void onResume() {
       super.onResume();

       //register broadcastreceiver  with Actions
       IntentFilter filter = new IntentFilter();
       filter.addAction(ACTION_GENRE_LIST_API_SUCCESS);
       filter.addAction(ACTION_GENRE_LIST_API_FAILURE);
       broadcastManager.registerReceiver(broadcastReceiver, filter);

       if(shouldReloadOnResume){
           loadGenres();
       }

       shouldReloadOnResume=false;
   }

    @Override
    public void onPause() {
        super.onPause();

        //un-register broadcastreceiver
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


    private void loadGenres(){

        isGenreLoaded = false;
        showLoading();

        Call<List<Genre>> call = genreService.getAllGenres();
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if(response.isSuccessful()) { //if(response.code()>=200 && response.code()<300)
                    Intent intent = new Intent(ACTION_GENRE_LIST_API_SUCCESS);
                    intent.putExtra(KEY_GENRES, response.body().toArray(new Genre[0]));
                    broadcastManager.sendBroadcast(intent);
                } /*else{
                    Intent intent = new Intent(ACTION_GENRE_LIST_API_SERVER_FAILURE);
                    intent.putExtra("STATUS", response.code());
                    intent.putExtra("BODY", response.errorBody().string());
                    broadcastManager.sendBroadcast(intent);
                }*/

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
        if (isGenreLoaded)
            hideLoading();
    }

    @Override
    public void onAction(int position, int action) {

        Genre g = genres.get(position);
        switch (action) {
            case GenreAdapter.ACTION_EDIT:
                shouldReloadOnResume = true;
                //Toast.makeText(GenreActivity.this, g.getName() + " Edit", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(GenreActivity.this, EditGenreActivity.class);
                intent.putExtra("genre_name", g.getName());
                intent.putExtra("genre_id", g.getId());
                startActivity(intent);*/
              //  Toast.makeText(getActivity(), "Edit clicked .", Toast.LENGTH_SHORT).show();
                listener.onGenreSelected(g.getName(),g.getId());
                break;

            case GenreAdapter.ACTION_DELETE:
                //  Toast.makeText(GenreActivity.this, g.getName() + " Delete", Toast.LENGTH_SHORT).show();
                Call<ResponseBody> call = genreService.deleteGenreEntry(g.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Toast.makeText(GenreActivity.this, "Sucessfully deleted entry",Toast.LENGTH_SHORT).show();
                        loadGenres();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                     //    Log.e(TAG, t.toString());
                    }
                });
                break;
        }

    }
}
