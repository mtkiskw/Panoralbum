package com.example.nttr.panobumapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by nttr on 2018/02/06.
 */

public class AlbumRecyclerViewAdapter extends RealmRecyclerViewAdapter<Image, AlbumRecyclerViewAdapter.AlbumListViewHolder> {
    private OnItemClickListener onItemClickListener = null;
    final private Context context;

    public AlbumRecyclerViewAdapter(RealmList<Image> images, Context context) {
        super(images, true);
        this.context = context;
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
        return new AlbumListViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(AlbumListViewHolder holder, int position) {
        Image image = getItem(position);
        if (image == null) {
            throw new IndexOutOfBoundsException("List size is " + getItemCount() + " but position is " + position);
        }
        if (image.uri != null) {
            try (InputStream stream = context.getContentResolver().openInputStream(Uri.parse(image.uri))) {
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
