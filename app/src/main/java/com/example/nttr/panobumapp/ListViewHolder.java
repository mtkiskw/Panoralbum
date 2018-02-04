package com.example.nttr.panobumapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ishikawa on 2018/02/04.
 */

public class ListViewHolder extends RecyclerView.ViewHolder{
    public TextView titleView;
    public TextView detailView;
    public ListViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        detailView = (TextView) itemView.findViewById(R.id.detail);

    }

}
