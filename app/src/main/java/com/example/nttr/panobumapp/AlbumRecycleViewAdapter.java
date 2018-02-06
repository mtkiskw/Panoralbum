package com.example.nttr.panobumapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nttr on 2018/02/06.
 */

public class AlbumRecycleViewAdapter extends RecyclerView.Adapter<AlbumListViewHolder>{
    private List<AlbumRowData> list;

    public AlbumRecycleViewAdapter(List<AlbumRowData> list)
    {
        this.list = list;
    }

    protected void onAlbumRecycleViewAdapterClicked(@NonNull AlbumRowData version) {
    }

    @Override
    public AlbumListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_row, parent,false);
        final AlbumListViewHolder vh = new AlbumListViewHolder(inflate);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                AlbumRowData version = list.get(position);
                onAlbumRecycleViewAdapterClicked(version);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(AlbumListViewHolder holder, int position) {
        holder.imageView.setImageBitmap(list.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
