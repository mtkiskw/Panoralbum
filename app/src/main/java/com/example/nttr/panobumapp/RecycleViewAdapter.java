package com.example.nttr.panobumapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ishikawa on 2018/02/04.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<ListViewHolder>{
    private List<RowData> list;

    public RecycleViewAdapter(List<RowData> list)
    {
        this.list = list;
    }

    protected void onRecycleViewAdapterClicked(@NonNull RowData version) {
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false);
        final ListViewHolder vh = new ListViewHolder(inflate);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                RowData version = list.get(position);
                onRecycleViewAdapterClicked(version);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.detailView.setText(list.get(position).getDetail());
        holder.imageView.setImageBitmap(list.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
