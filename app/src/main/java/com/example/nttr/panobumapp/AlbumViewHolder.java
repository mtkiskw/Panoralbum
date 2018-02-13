package com.example.nttr.panobumapp;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ishikawa on 2018/02/04.
 */

public class AlbumViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public ImageView imageView;
    public CircleTextView circleTextView;

    public AlbumViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.representative_img);
        circleTextView = itemView.findViewById(R.id.detail);
    }

    public void setColorFilterToImg(View itemView) {
        imageView = itemView.findViewById(R.id.representative_img);
        imageView.setColorFilter(new LightingColorFilter(Color.LTGRAY, 0));
    }
}
