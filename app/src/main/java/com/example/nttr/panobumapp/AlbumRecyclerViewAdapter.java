package com.example.nttr.panobumapp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by nttr on 2018/02/06.
 */

public class AlbumRecyclerViewAdapter extends RealmRecyclerViewAdapter<Image, AlbumRecyclerViewAdapter.AlbumListViewHolder> {
    private OnItemClickListener onItemClickListener = null;

    public AlbumRecyclerViewAdapter(RealmList<Image> images) {
        super(images, true);
    }

    private void setOnItemClickListener(@Nullable OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    private void onAlbumRecycleViewAdapterClicked(@NonNull Image image) {
        if(onItemClickListener != null){
            onItemClickListener.onClick(image);
        }
    }

    @Override
    public AlbumListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_row, parent, false);
        final AlbumListViewHolder vh = new AlbumListViewHolder(inflate);

        return vh;
    }

    @Override
    public void onBindViewHolder(AlbumListViewHolder holder, int position) {
        Image image = getItem(position);
        if (image == null) {
            throw new IndexOutOfBoundsException("List size is " + getItemCount() + " but position is " + position);
        }
        Uri.parse(image.uri);
        holder.imageView.setImageURI(Uri.parse(image.uri));
        holder.itemView.setOnClickListener(v -> onAlbumRecycleViewAdapterClicked(image));
    }

    public interface OnItemClickListener {
        void onClick(@NonNull Image image);
    }

    public class AlbumListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public AlbumListViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.representative_img);
        }
    }
}
