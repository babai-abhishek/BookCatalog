package com.example.abhishek.bookcatalogwithfragment.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.bookcatalogwithfragment.Model.Genre;
import com.example.abhishek.bookcatalogwithfragment.R;

import java.util.List;

/**
 * Created by abhishek on 5/3/18.
 */

public class SelectGenreAdapter extends RecyclerView.Adapter<SelectGenreAdapter.GenreTypeViewHolder> {

    private List<Genre> genreList;
    private GenreSelectionListener listener;

    public SelectGenreAdapter(List<Genre> list, GenreSelectionListener listener) {
        this.genreList = list;
        this.listener = listener;
    }


    public interface GenreSelectionListener{
        void onSelectGenre(Genre genre);
    }


    @Override
    public GenreTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.select_genre_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        GenreTypeViewHolder genreTypeViewHolder = new GenreTypeViewHolder(view);
        return genreTypeViewHolder;
    }

    @Override
    public void onBindViewHolder(GenreTypeViewHolder holder, int position) {
        final Genre genre = genreList.get(position);
        holder.bind(genre);
        holder.genreTypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectGenre(genre);
            }
        });

    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public void setGenreList(List<Genre> genres) {
        this.genreList=genres;
        notifyDataSetChanged();
    }

    class GenreTypeViewHolder extends RecyclerView.ViewHolder{

        TextView genreTypeTextView;

        public GenreTypeViewHolder(View itemView) {
            super(itemView);

            genreTypeTextView = (TextView) itemView.findViewById(R.id.select_genre_type_list_item);
        }

        public void bind(Genre genre) {
            genreTypeTextView.setText(genre.getName());
        }
    }
}

