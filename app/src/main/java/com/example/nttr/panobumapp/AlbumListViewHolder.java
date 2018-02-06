package com.example.nttr.panobumapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by nttr on 2018/02/06.
 */

public class AlbumListViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public AlbumListViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.representative_img);
    }
}
