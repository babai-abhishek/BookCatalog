package com.example.abhishek.bookcatalogwithfragment.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.abhishek.bookcatalogwithfragment.R;
import com.example.abhishek.bookcatalogwithfragment.models.bl.GenreBusinessModel;

import java.util.List;

/**
 * Created by abhishek on 8/11/17.
 */

public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ACTION_EDIT = 0;
    public static final int ACTION_DELETE = 1;
    private boolean shownAsDialog = false;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private List<GenreBusinessModel> genreList;
    private String TAG = "#";

    private boolean isLoading=false;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyDataSetChanged();
    }

    private ListItemClickListener listItemClickListener;
    private SelectFromDialog selectFromDialog;

    public GenreAdapter(List<GenreBusinessModel> list, ListItemClickListener listItemClickListener) {
        this.genreList = list;
        this.listItemClickListener = listItemClickListener;
    }
    public GenreAdapter(List<GenreBusinessModel> list, SelectFromDialog selectFromDialog) {
        this.genreList = list;
        this.selectFromDialog = selectFromDialog;
    }

    public interface SelectFromDialog {
        void onSelect(GenreBusinessModel genre);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = inflater.inflate(R.layout.genre_list_item,
                        parent, false);
                GenreViewHolder genreViewHolder = new GenreViewHolder(view);
                return genreViewHolder;
            default:
                view = inflater.inflate(R.layout.genre_list_empty_item,
                        parent, false);
                EmptyViewHolder emptyViewHolder = new EmptyViewHolder(view);
                return emptyViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rvHolder, int position) {

        if (genreList.size() < 1 || isLoading()) {

            ((EmptyViewHolder)rvHolder).message.setText(isLoading()?"Loading...":"No items found!");
            //show a template saying no genre found. and give a button to add a new genre

        } else {
            GenreViewHolder holder = (GenreViewHolder) rvHolder;
            final GenreBusinessModel genre = genreList.get(position);
            holder.bind(genre);
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemClickListener.onAction(genreList.indexOf(genre), ACTION_EDIT);
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemClickListener.onAction(genreList.indexOf(genre), ACTION_DELETE);
                }
            });
            if(shownAsDialog){
                holder.genre_list_item_cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFromDialog.onSelect(genre);
                    }
                });
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (genreList.size() < 1 || isLoading())
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        int genreSize = genreList.size();
        return genreSize < 1 || isLoading() ? 1 : genreSize;
    }

    public void setGenreList(List<GenreBusinessModel> genres) {
        this.genreList = genres;
        notifyDataSetChanged();
    }

    public void setGenreListForDialog(List<GenreBusinessModel> genres) {
        this.genreList = genres;
        notifyDataSetChanged();
        shownAsDialog = true;
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView genreNameTextView, genreIdTextView, tv_genre_list_id_header, tv_genre_list_name_header;
        Button btnEdit, btnDelete;
        CardView genre_list_item_cardView;

        public GenreViewHolder(View itemView) {
            super(itemView);

            genre_list_item_cardView = (CardView) itemView.findViewById(R.id.genre_list_item_cardView);
            tv_genre_list_id_header = (TextView) itemView.findViewById(R.id.tv_genre_list_id_header);
            tv_genre_list_name_header = (TextView) itemView.findViewById(R.id.tv_genre_list_name_header);
            genreNameTextView = (TextView) itemView.findViewById(R.id.genre_list_item_name);
            genreIdTextView = (TextView) itemView.findViewById(R.id.genre_list_item_id);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            if(shownAsDialog){
                btnEdit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                genreIdTextView.setVisibility(View.GONE);
                tv_genre_list_name_header.setVisibility(View.GONE);
                tv_genre_list_id_header.setVisibility(View.GONE);
            }
        }

        void bind(final GenreBusinessModel genre) {
            genreNameTextView.setText(genre.getName());
            genreIdTextView.setText(genre.getId());
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        public EmptyViewHolder(View view) {
            super(view);
            message= (TextView) view.findViewById(R.id.message);
        }
    }
}
