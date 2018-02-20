package com.example.nttr.panobumapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity{
    private ArrayList<Uri> selectedUris;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ImageLoadingTask backgroundImageLoaderTask;
    private VrPanoramaView panoWidgetView;
    private int nowPlaying = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        panoWidgetView = (VrPanoramaView) findViewById(R.id.pano_view);
        panoWidgetView.setEventListener(vrPanoramaEventListener);

        selectedUris = (ArrayList<Uri>)getIntent().getSerializableExtra("selectedUris");
        startPlaying(selectedUris);
    }

    private VrPanoramaEventListener vrPanoramaEventListener = new VrPanoramaEventListener(){
        @Override
        public void onClick() {
            super.onClick();
            if((nowPlaying + 1) < bitmaps.size()) {
                toNext(nowPlaying + 1);
                nowPlaying = nowPlaying + 1;
            }
            else{
                toNext(0);
                nowPlaying = 0;
            }
        }
    };

    private void startPlaying(ArrayList<Uri> uris){
        for(int i=0; i<uris.size(); i++){
            Log.d("URI", uris.get(0).toString());
            Log.d("PATH", uris.get(0).getPath().toString());

        try(InputStream stream = this.getContentResolver().openInputStream(uris.get(0)) ){
                Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uris.get(i));
                bitmaps.add(b);
            }
            catch (IOException e){

            }
        }
        toNext(0);
        nowPlaying = 0;
    }

    private void toNext(int index){
        ImageLoadingTask task = backgroundImageLoaderTask;
        if (task != null && !task.isCancelled()) {
            // Cancel any task from a previous loading.
            task.cancel(true);
        }
        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        task = new ImageLoadingTask(panoWidgetView, viewOptions);
        task.execute(bitmaps.get(index));
        backgroundImageLoaderTask = task;
    }

    private void resetAlbum(){
        bitmaps = new ArrayList<>();
        nowPlaying = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Bitmap bitmap: bitmaps) {
            if(bitmap != null){
                bitmap.recycle();
            }
        }
    }
}
