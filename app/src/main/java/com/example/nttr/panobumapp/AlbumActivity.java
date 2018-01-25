package com.example.nttr.panobumapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {
    private ArrayList<Uri> selectedUris;
    private ArrayList<Bitmap> bitmaps;
    private ImageLoadingTask backgroundImageLoaderTask;
    private VrPanoramaView panoWidgetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

//        Intent intent = new Intent(getApplication(), MainActivity.class);
//        startActivity(intent);

        selectedUris = (ArrayList<Uri>)getIntent().getSerializableExtra("selectedUris");
        try {
            startPlaying(selectedUris);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Button transActivityBtn = (Button) findViewById(R.id.show_album_btn);
//        transActivityBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplication(), MainActivity.class);
//                startActivity(intent);
//            }
//
//        });
    }

    private void startPlaying(ArrayList<Uri> uris) throws IOException {
        for(int i=0; i<uris.size(); i++){
            Log.d("URI", uris.get(i).toString());
            Log.d("PATH", uris.get(i).getPath().toString());
            try {
                Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uris.get(i));
                bitmaps.add(b);
            }
            catch (IOException e){

            }
        }

//        ImageLoadingTask task = backgroundImageLoaderTask;
//        if (task != null && !task.isCancelled()) {
//            // Cancel any task from a previous loading.
//            task.cancel(true);
//        }
//        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
//        viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
//        task = new ImageLoadingTask(panoWidgetView, viewOptions);
//        task.execute(bitmaps.get(0));
//        backgroundImageLoaderTask = task;
    }

//    private File getFilePath(Uri uri){
//        String[] projection = {MediaStore.MediaColumns.DATA};
//        Cursor cursor = getContext.getContentResolver().query(uri, projection, null, null, null);
//        File file = null;
//        if (cursor != null) {
//            String path = null;
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(0);
//            }
//            cursor.close();
//            if (path != null) {
//                file = new File(path);
//            }
//        }
//        return file;
//    }
}
