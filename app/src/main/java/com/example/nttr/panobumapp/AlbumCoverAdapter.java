package com.example.nttr.panobumapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by ishikawa on 2018/02/04.
 */

public class AlbumCoverAdapter extends RealmRecyclerViewAdapter<Album, AlbumViewHolder> {
    final private Context context;

    @Nullable
    private OnItemClickListener onItemClickListener = null;

    public AlbumCoverAdapter(RealmResults<Album> albums, Context context) {
        super(albums, true);
        this.context = context;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private void onRecycleViewAdapterClicked(@NonNull AlbumCover albumCover, AlbumViewHolder viewHolder) {
        viewHolder.setColorFilterToImg(viewHolder.itemView);
        if (onItemClickListener != null) {
            onItemClickListener.onClick(albumCover, viewHolder);
        }
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        final AlbumViewHolder vh = new AlbumViewHolder(inflated);
        vh.itemView.setOnClickListener(v -> {
            final int position = vh.getAdapterPosition();
            Album album = getItem(position);
            if(album != null) {
                AlbumCover version = album.toAlbumCover();
                onRecycleViewAdapterClicked(version, vh);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = getItem(position);
        if (album == null) return;

        AlbumCover albumCover = album.toAlbumCover();
        holder.titleView.setText(albumCover.getTitle());
        holder.circleTextView.setText(albumCover.getDetail());
        holder.circleTextView.setSolidColor("#696969");
        Uri imageUri = albumCover.getCoverImageUri();
        if (imageUri != null) {
            try (InputStream stream = context.getContentResolver().openInputStream(albumCover.getCoverImageUri())) {
                if (stream != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inSampleSize = 4;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
                    holder.imageView.setImageBitmap(bitmap);
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        holder.imageView.setImageResource(R.drawable.dummy_image);
    }

    public interface OnItemClickListener {
        void onClick(@NonNull AlbumCover albumCover, AlbumViewHolder viewHolder);
    }

}
