package com.example.nttr.panobumapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.vr.sdk.widgets.common.VrEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;


public class MainActivity extends AppCompatActivity {
    private VrPanoramaView panoWidgetView;
    private ImageLoaderTask backgroundImageLoaderTask;

    private VrEventListener vrEventListener;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panoWidgetView = (VrPanoramaView) findViewById(R.id.pano_view);
//        panoWidgetView.setTouchTrackingEnabled(true);
//        vrEventListener = new VrEventListener();
//        panoWidgetView.setOnTouchListener(this);

        panoWidgetView.setEventListener(vrPanoramaEventListener);
        loadImage();

    }

    private VrPanoramaEventListener vrPanoramaEventListener = new VrPanoramaEventListener(){
        @Override
        public void onClick() {
            super.onClick();
            Log.e(TAG, "Touched");
        }
    };

    @Override
    public void onPause() {
        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    public void onResume() {
        panoWidgetView.resumeRendering();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        panoWidgetView.shutdown();
        super.onDestroy();
    }

    private void loadImage() {
        ImageLoaderTask task = backgroundImageLoaderTask;
        if (task != null && !task.isCancelled()) {
            // Cancel any task from a previous loading.
            task.cancel(true);
        }

        // pass in the name of the image to load from assets.
        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;

        // use the name of the image in the assets/ directory.
        String panoImageName = "pano.jpg";

        // create the task passing the widget view and call execute to start.
        task = new ImageLoaderTask(panoWidgetView, viewOptions, panoImageName);
        task.execute(getAssets());
        backgroundImageLoaderTask = task;

    }
}
