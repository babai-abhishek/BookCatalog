package com.example.abhishek.bookcatalogwithfragment.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.abhishek.bookcatalogwithfragment.Book.BookListFragment;
import com.example.abhishek.bookcatalogwithfragment.Model.ApiModel.DummyBook;
import com.example.abhishek.bookcatalogwithfragment.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 17/1/18.
 */

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   // private List<Book> bookList;
    private SelectBookFromListListener listener;
    private List<DummyBook> dummyBooks;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private boolean isLoading = false;
    private boolean shownOnlyAsList = false;

    public BookAdapter(ArrayList<DummyBook> dummyBooks, BookListFragment listListener, boolean shownOnlyAsList) {
        this.dummyBooks = dummyBooks;
        this.listener = listListener;
        this.shownOnlyAsList = shownOnlyAsList;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return isLoading;
    }

    /*public BookAdapter(List<Book> bookList, SelectBookFromListListener listListener, boolean shownOnlyAsList) {
        this.bookList = bookList;
        this.listener = listListener;
        this.shownOnlyAsList = shownOnlyAsList;
    }
*/
    public void setBookList(ArrayList<DummyBook> dummyBooks) {
        this.dummyBooks = dummyBooks;
        notifyDataSetChanged();
    }

    public interface SelectBookFromListListener{
         void onSelectBook(int position, ImageView ivBook);
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if(dummyBooks.size()<1 || isLoading())
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType){
            case VIEW_TYPE_ITEM:
                view = inflater.inflate( R.layout.book_list_item,
                        parent,false);
                BookViewHolder bookViewHolder = new BookViewHolder(view);
                return bookViewHolder;
            default:
                view = inflater.inflate(R.layout.book_list_empty_item,
                        parent, false);
                EmptyListViewHolder emptyListViewHolder = new EmptyListViewHolder(view);
                return emptyListViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (dummyBooks.size() < 1 || isLoading()) {
            ((EmptyListViewHolder) holder).emptyListMessage.setText(isLoading() ? "Loading...." : "No items found!");
        }
        else {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;
            final DummyBook book = dummyBooks.get(position);
            bookViewHolder.Bind(book);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((BookViewHolder) holder).ivBook.setTransitionName("transition" + position);
            }
            if(!shownOnlyAsList){
                bookViewHolder.bookListCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onSelectBook(dummyBooks.indexOf(book), ((BookViewHolder) holder).ivBook);
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        int bookListSize = dummyBooks.size();
        return bookListSize<1 || isLoading() ? 1 : bookListSize;
    }

   /* public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }
*/
    class BookViewHolder extends RecyclerView.ViewHolder{

        TextView tvBookName,tv_book_publishDate_list;
        CardView bookListCardView;
        ImageView ivBook;

        public BookViewHolder(View itemView) {
            super(itemView);

            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name_list);
            tv_book_publishDate_list = (TextView) itemView.findViewById(R.id.tv_book_publishDate_list);
            bookListCardView = (CardView) itemView.findViewById(R.id.book_list_item_cardView);
            ivBook = (ImageView) itemView.findViewById(R.id.ivBook);
        }

        void Bind(DummyBook book){
            tvBookName.setText(book.getBook().getName());
            tv_book_publishDate_list.setText("Published on : "+book.getBook().getPublished());
            Picasso.get().load(book.getImageUrl()).into(ivBook);
        }
    }

    private class EmptyListViewHolder extends RecyclerView.ViewHolder {

        TextView emptyListMessage;

        public EmptyListViewHolder(View itemView) {
            super(itemView);
            emptyListMessage = (TextView) itemView.findViewById(R.id.book_empty_message);

        }
    }
}
