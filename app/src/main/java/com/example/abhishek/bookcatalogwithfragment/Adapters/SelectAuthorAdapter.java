package com.example.abhishek.bookcatalogwithfragment.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.R;

import java.util.List;

/**
 * Created by abhishek on 15/1/18.
 */

public class SelectAuthorAdapter extends RecyclerView.Adapter<SelectAuthorAdapter.AuthorNameViewHolder>{

    private List<Author> authorList;
    private AuthorSelectionListener listener;
    public SelectAuthorAdapter(List<Author> list, AuthorSelectionListener listener) {
        this.authorList = list;
        this.listener = listener;
    }

    public interface AuthorSelectionListener{
        void onSelectAuthor(Author author);
    }

    @Override
    public AuthorNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.select_author_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        AuthorNameViewHolder nameViewHolder = new AuthorNameViewHolder(view);
        return nameViewHolder;
    }

    @Override
    public void onBindViewHolder(AuthorNameViewHolder holder, int position) {
        final Author author = authorList.get(position);
        holder.bind(author);
        holder.authorNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectAuthor(author);
            }
        });

    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public void setAuthorList(List<Author> authorNames) {
        this.authorList=authorNames;
        notifyDataSetChanged();
    }

    class AuthorNameViewHolder extends RecyclerView.ViewHolder{

        TextView authorNameTextView;

        public AuthorNameViewHolder(View itemView) {
            super(itemView);
            authorNameTextView = (TextView) itemView.findViewById(R.id.tv_select_author_name_for_book);
        }

        public void bind(Author author) {
            authorNameTextView.setText(author.getName());
        }

    }
}
