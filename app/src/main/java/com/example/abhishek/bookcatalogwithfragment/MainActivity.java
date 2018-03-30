package com.example.abhishek.bookcatalogwithfragment;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.abhishek.bookcatalogwithfragment.Author.AuthorAddFragment;
import com.example.abhishek.bookcatalogwithfragment.Author.AuthorEditFragment;
import com.example.abhishek.bookcatalogwithfragment.Author.AuthorListFragment;
import com.example.abhishek.bookcatalogwithfragment.Book.BookAddFragment;
import com.example.abhishek.bookcatalogwithfragment.Book.BookDetailsFragment;
import com.example.abhishek.bookcatalogwithfragment.Book.BookEditFragment;
import com.example.abhishek.bookcatalogwithfragment.Book.BookListFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreAddFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreEditFragment;
import com.example.abhishek.bookcatalogwithfragment.Genre.GenreListFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.RealmModel.Author;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.DummyBook;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.RealmModel.Genre;
import com.example.abhishek.bookcatalogwithfragment.models.bl.AuthorBusinessModel;
import com.example.abhishek.bookcatalogwithfragment.models.bl.GenreBusinessModel;

public class MainActivity extends AppCompatActivity implements
        OptionsFragment.OptionsFragmentInteractionListener,
        GenreListFragment.GenreListFragmentInteractionListener,
        GenreListFragment.GenreFabuttonClickListener,
        AuthorListFragment.AuthorListFragmentInteractionListener,
        AuthorListFragment.AuthorFabButtonClickListener,
        BookListFragment.BookFabButtonClickListener,
        BookListFragment.BookListFragmentInteractionListener ,
        BookDetailsFragment.GetAllBooksOfParticularGenre,
        BookDetailsFragment.GetAllBooksOfParticularAuthor,
        BookDetailsFragment.BookDetailsEditButtonClickListener {

    private static final String TAG_FRAGMENT_OPTIONS="OptionsFragment";

    private static final String TAG_FRAGMENT_BOOK_LIST="BookListFragment";
    private static final String TAG_FRAGMENT_BOOK_ADD = "BookAddFragment";
    private static final String TAG_FRAGMENT_BOOK_DETAILS = "BookDetailsFragment";
    private static final String TAG_FRAGMENT_BOOK_EDIT = "BookEditFragment";


    private static final String TAG_FRAGMENT_GENRE_LIST="GenreListFragment";
    private static final String TAG_FRAGMENT_GENRE_EDIT ="GenreEditFragment";
    private static final String TAG_FRAGMENT_GENRE_ADD ="GenreAddFragment";

    private static final String TAG_FRAGMENT_AUTHOR_LIST="AuthorListFragment";
    private static final String TAG_FRAGMENT_AUTHOR_EDIT ="AuthorEditFragment";
    private static final String TAG_FRAGMENT_AUTHOR_ADD ="AuthorAddFragment";

    private FrameLayout flFragmentContainer;
    private FragmentManager manager;

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
    public void onGenreSelected(GenreBusinessModel genre) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, GenreEditFragment.getInstance(genre), TAG_FRAGMENT_GENRE_EDIT)
                .addToBackStack(TAG_FRAGMENT_GENRE_EDIT)
                .commit();
    }

    @Override
    public void onGenreFabClick() {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, new GenreAddFragment(), TAG_FRAGMENT_GENRE_ADD)
                .addToBackStack(TAG_FRAGMENT_GENRE_ADD)
                .commit();
    }

    @Override
    public void onAuthorSelected(String authName, String authId, String authLanguage, String authCountry) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, AuthorEditFragment.getInstance(authName, authId, authLanguage, authCountry), TAG_FRAGMENT_AUTHOR_EDIT)
                .addToBackStack(TAG_FRAGMENT_AUTHOR_EDIT)
                .commit();
    }

    @Override
    public void onAuthorFabClick() {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, new AuthorAddFragment(), TAG_FRAGMENT_AUTHOR_ADD)
                .addToBackStack(TAG_FRAGMENT_AUTHOR_ADD)
                .commit();

    }

    @Override
    public void onBookFabClick() {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, new BookAddFragment(), TAG_FRAGMENT_BOOK_ADD)
                .addToBackStack(TAG_FRAGMENT_BOOK_ADD)
                .commit();

    }
/*
    @Override
    public void onBookSelected(Book book) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, BookDetailsFragment.getInstance(book), TAG_FRAGMENT_BOOK_DETAILS)
                .addToBackStack(TAG_FRAGMENT_BOOK_DETAILS)
                .commit();
    }*/

    @Override
    public void onParticularGenreSelected(GenreBusinessModel genre) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, BookListFragment.newInstanceForGenre(genre.getId()), TAG_FRAGMENT_BOOK_LIST)
                .addToBackStack(TAG_FRAGMENT_BOOK_LIST)
                .commit();
    }

    @Override
    public void onParticularAuthorSelected(AuthorBusinessModel author) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, BookListFragment.newInstanceForAuthor(author.getId()), TAG_FRAGMENT_BOOK_LIST)
                .addToBackStack(TAG_FRAGMENT_BOOK_LIST)
                .commit();
    }

    @Override
    public void onEditClicked(DummyBook book, GenreBusinessModel genre, AuthorBusinessModel author) {
        manager.beginTransaction()
                .replace(R.id.flFragmentContainer, BookEditFragment.newInstance(book, genre, author), TAG_FRAGMENT_BOOK_EDIT)
                .addToBackStack(TAG_FRAGMENT_BOOK_EDIT)
                .commit();
    }

    @Override
    public void onBookSelected(BookListFragment current, int position, DummyBook book, ImageView view) {
       /* manager.beginTransaction()
                .replace(R.id.flFragmentContainer, BookDetailsFragment.getInstance(book), TAG_FRAGMENT_BOOK_DETAILS)
                .addToBackStack(TAG_FRAGMENT_BOOK_DETAILS)
                .commit();*/
        BookDetailsFragment newFragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("transitionName", "transition" + position);
        bundle.putSerializable("book", book);
        newFragment.setArguments(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            current.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
            current.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));

            newFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
            newFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
        }

        manager.beginTransaction()
        .replace(R.id.flFragmentContainer, newFragment, TAG_FRAGMENT_BOOK_DETAILS)
        .addToBackStack(TAG_FRAGMENT_BOOK_DETAILS)
        .addSharedElement(view, "transition" + position)
        .commit();
    }
}
