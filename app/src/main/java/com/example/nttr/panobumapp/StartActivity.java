package com.example.nttr.panobumapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    private Button openButton;
    private static final int REQUEST_CODE_CHOOSE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        openButton = (Button) findViewById(R.id.open_folder_button);
        openButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.open_folder_button:
                Log.d("LOG", "button clicked");
                Matisse.from(StartActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(9)
                        .addFilter(new GifSizeFilter(320,320, 5* Filter.K*Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.photo_grid_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
        }

    }
}
