package com.example.abhishek.bookcatalogwithfragment.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.abhishek.bookcatalogwithfragment.Model.Author;
import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.R;

import java.util.List;

/**
 * Created by abhishek on 20/2/18.
 */

public class AuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Author> authorList;
    public static final int ACTION_EDIT = 0;
    public static final int ACTION_DELETE = 1;
    private boolean shownAsDialog = false;


    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private boolean isLoading = false;

    ListItemClickListener listItemClickListener;
    private SelectFromDialog selectFromDialog;


    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public AuthorAdapter(List<Author> authorListlist, ListItemClickListener listItemClickListener) {
        this.authorList = authorListlist;
        this.listItemClickListener = listItemClickListener;
    }

    public AuthorAdapter(List<Author> list, AuthorAdapter.SelectFromDialog selectFromDialog) {
        this.authorList = list;
        this.selectFromDialog = selectFromDialog;
    }

    public interface SelectFromDialog {
        void onSelect(Author author);
    }


    @Override
    public int getItemViewType(int position) {
        /*return super.getItemViewType(position);*/
        if (authorList.size() < 1 || isLoading())
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = inflater.inflate(R.layout.author_list_item,
                        parent, false);
                AuthorViewHolder authorViewHolder = new AuthorViewHolder(view);
                return authorViewHolder;

            default:
                view = inflater.inflate(R.layout.author_list_empty_item,
                        parent, false);
                EmptyListViewHolder emptyListViewHolder = new EmptyListViewHolder(view);
                return emptyListViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (authorList.size() < 1 || isLoading()) {
            ((EmptyListViewHolder) holder).emptyListMessage.setText(isLoading() ? "Loading...." : "No items found!");
        } else {
            AuthorViewHolder authorViewHolder = (AuthorViewHolder) holder;
            final Author author = authorList.get(position);
            authorViewHolder.bind(author);
            authorViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemClickListener.onAction(authorList.indexOf(author), ACTION_EDIT);
                }
            });
            authorViewHolder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemClickListener.onAction(authorList.indexOf(author), ACTION_DELETE);
                }
            });
            if(shownAsDialog){
                ((AuthorViewHolder) holder).author_list_item_cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFromDialog.onSelect(author);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int authorListSize = authorList.size();
        return authorListSize < 1 || isLoading() ? 1 : authorListSize;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
        notifyDataSetChanged();
    }

    public void setAuthorListForDialog(List<Author> authors) {
        this.authorList = authors;
        notifyDataSetChanged();
        shownAsDialog = true;
    }

    class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView tv_auth_name, tv_auth_id, tv_auth_language, tv_auth_country,
                tv_author_list_id_header, tv_author_list_name_header, tv_author_list_language_header, tv_author_list_country_header ;
        Button btnEdit, btnDel;
        CardView author_list_item_cardView;

        public AuthorViewHolder(View itemView) {
            super(itemView);

            tv_author_list_id_header = (TextView) itemView.findViewById(R.id.tv_author_list_id_header);
            tv_author_list_name_header = (TextView) itemView.findViewById(R.id.tv_author_list_name_header);
            tv_author_list_language_header = (TextView) itemView.findViewById(R.id.tv_author_list_language_header);
            tv_author_list_country_header = (TextView) itemView.findViewById(R.id.tv_author_list_country_header);
            author_list_item_cardView = (CardView) itemView.findViewById(R.id.author_list_item_cardView);
            tv_auth_name = (TextView) itemView.findViewById(R.id.tv_author_name);
            tv_auth_id = (TextView) itemView.findViewById(R.id.tv_author_id);
            tv_auth_language = (TextView) itemView.findViewById(R.id.tv_author_language);
            tv_auth_country = (TextView) itemView.findViewById(R.id.tv_author_country);

            btnEdit = (Button) itemView.findViewById(R.id.btn_author_edit);
            btnDel = (Button) itemView.findViewById(R.id.btn_author_delete);

            if(shownAsDialog){
                btnEdit.setVisibility(View.GONE);
                btnDel.setVisibility(View.GONE);
                tv_auth_id.setVisibility(View.GONE);
                tv_auth_language.setVisibility(View.GONE);
                tv_auth_country.setVisibility(View.GONE);
                tv_author_list_id_header.setVisibility(View.GONE);
                tv_author_list_name_header.setVisibility(View.GONE);
                tv_author_list_language_header.setVisibility(View.GONE);
                tv_author_list_country_header.setVisibility(View.GONE);
            }
        }

        void bind(final Author author) {
            tv_auth_name.setText(author.getName());
            tv_auth_id.setText(author.getId());
            tv_auth_language.setText(author.getLanguage());
            tv_auth_country.setText(author.getCountry());
        }
    }

    private class EmptyListViewHolder extends RecyclerView.ViewHolder {

        TextView emptyListMessage;

        public EmptyListViewHolder(View itemView) {
            super(itemView);
            emptyListMessage = (TextView) itemView.findViewById(R.id.author_empty_message);

        }
    }
}

