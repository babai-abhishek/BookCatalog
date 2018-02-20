package com.example.abhishek.bookcatalogwithfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.abhishek.bookcatalogwithfragment.Author.AuthorListFragment;
import com.example.abhishek.bookcatalogwithfragment.Book.BookListFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreEditFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreListFragment;

public class MainActivity extends AppCompatActivity implements
        OptionsFragment.OptionsFragmentInteractionListener,
        GenreListFragment.GenreListFragmentInteractionListener{

    private static final String TAG_FRAGMENT_OPTIONS="OptionsFragment";
    private static final String TAG_FRAGMENT_GENRE_LIST="GenreListFragment";
    private static final String TAG_FRAGMENT_AUTHOR_LIST="AuthorListFragment";
    private static final String TAG_FRAGMENT_BOOK_LIST="BookListFragment";
    private static final String TAG_FRAGMENT_EDIT_GENRE ="GenreEditFragment";

    /*private static String KEY_RESTORE_GENRE_LIST_FRAGMENT = "restoreGenreFragment";
    private static boolean restoreGenreFragment = false;*/
   // Button btnToggle;
    private FrameLayout flFragmentContainer;
    private FragmentManager manager;
    OptionsFragment optionsFragment ;
    GenreListFragment genreListFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //btnToggle=findViewById(R.id.btnToggle);
        flFragmentContainer= (FrameLayout) findViewById(R.id.flFragmentContainer);

        manager = getSupportFragmentManager();

        //if(savedInstanceState == null){
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.flFragmentContainer, new OptionsFragment(),TAG_FRAGMENT_OPTIONS);
            transaction.commit();
       /* }
        else {
            if(restoreGenreFragment=savedInstanceState.getBoolean(KEY_RESTORE_GENRE_LIST_FRAGMENT)){
                genreListFragment = (GenreListFragment) manager.findFragmentByTag(TAG_FRAGMENT_GENRE_LIST);
            }
            else {
                optionsFragment = (OptionsFragment) manager.findFragmentByTag(TAG_FRAGMENT_OPTIONS);
            }
        }*/
        /*btnToggle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //
                Fragment fragment = manager.findFragmentById(R.id.flFragmentContainer);
                //Log.d("TEST", fragment.getClass().getSimpleName());
                if(fragment instanceof OptionsFragment){
                    manager.beginTransaction()
                            .replace(R.id.flFragmentContainer, new GenreListFragment(), TAG_FRAGMENT_GENRE_LIST)
                            .addToBackStack(TAG_FRAGMENT_GENRE_LIST)
                            .commit();
                } else if(fragment instanceof GenreListFragment){
                    manager.beginTransaction()
                            .replace(R.id.flFragmentContainer, new OptionsFragment(), TAG_FRAGMENT_OPTIONS)
                            .addToBackStack(TAG_FRAGMENT_OPTIONS)
                            .commit();
                }
            }
        });*/
    }

  /*  @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_RESTORE_GENRE_LIST_FRAGMENT,restoreGenreFragment);
    }
*/
    @Override
    public void onGenreClicked(Fragment fragment) {
          //  restoreGenreFragment = true;
            manager.beginTransaction()
                    .replace(R.id.flFragmentContainer, new GenreListFragment(), TAG_FRAGMENT_GENRE_LIST)
                    .addToBackStack(TAG_FRAGMENT_GENRE_LIST)
                    .commit();

    }

    @Override
    public void onAuthorClicked(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, new AuthorListFragment(), TAG_FRAGMENT_AUTHOR_LIST)
                .addToBackStack(TAG_FRAGMENT_AUTHOR_LIST)
                .commit();
    }

    @Override
    public void onBookClicked(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, new BookListFragment(), TAG_FRAGMENT_BOOK_LIST)
                .addToBackStack(TAG_FRAGMENT_BOOK_LIST)
                .commit();
    }

    @Override
    public void onGenreSelected(String genreName, String genreId) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, GenreEditFragment.getInstance(genreName, genreId), TAG_FRAGMENT_EDIT_GENRE)
                .addToBackStack(TAG_FRAGMENT_EDIT_GENRE)
                .commit();
    }
}
