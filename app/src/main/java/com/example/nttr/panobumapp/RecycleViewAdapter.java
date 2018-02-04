package com.example.nttr.panobumapp;

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

    public RecycleViewAdapter(List<RowData> list) {
        this.list = list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false);
        ListViewHolder vh = new ListViewHolder(inflate);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.detailView.setText(list.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
