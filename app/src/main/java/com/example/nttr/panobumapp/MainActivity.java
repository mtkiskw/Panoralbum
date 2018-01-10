package com.example.nttr.panobumapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;


public class MainActivity extends AppCompatActivity {
    private VrPanoramaView panoWidgetView;
    private ImageLoaderTask backgroundImageLoaderTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panoWidgetView = (VrPanoramaView) findViewById(R.id.pano_view);

        loadImage();

    }

    private void loadImage() {

    }
}
