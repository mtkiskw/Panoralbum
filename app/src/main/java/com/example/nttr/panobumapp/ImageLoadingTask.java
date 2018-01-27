package com.example.nttr.panobumapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.lang.ref.WeakReference;

/**
 * Created by ishikawa on 2018/01/25.
 */

public class ImageLoadingTask extends AsyncTask<Bitmap, Void, Bitmap>{
    private static final String TAG = "ImageLoadingTask";
    private final WeakReference<VrPanoramaView> viewReference; // 画像読み込み時にViewが壊れるのを防ぐ（回転させた時とか）
    private final VrPanoramaView.Options viewOptions;

    private static WeakReference<Bitmap> lastBitmap = new WeakReference<>(null);

    protected Bitmap doInBackground(Bitmap... bitmaps) {
        Bitmap b = bitmaps[0];
        lastBitmap = new WeakReference<>(b);
        return b;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        final VrPanoramaView vw = viewReference.get();
        if (vw != null && bitmap != null) {
            vw.loadImageFromBitmap(bitmap, viewOptions);
        }
    }

    public ImageLoadingTask(VrPanoramaView view, VrPanoramaView.Options viewOptions) {
        viewReference = new WeakReference<>(view);
        this.viewOptions = viewOptions;
    }
}
