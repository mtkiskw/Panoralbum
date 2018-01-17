package com.example.nttr.panobumapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {
    private ArrayList<Uri> selectedUris;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        selectedUris = (ArrayList<Uri>)getIntent().getSerializableExtra("selectedUris");
        showUris(selectedUris);

        Button transActivityBtn = (Button) findViewById(R.id.show_album_btn);
        transActivityBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }

        });
    }

    private void showUris(ArrayList<Uri> uris){
        for(int i=0; i<uris.size(); i++){
            Log.d("URI", uris.get(i).toString());
            Log.d("PATH", uris.get(i).getPath().toString());
        }
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
