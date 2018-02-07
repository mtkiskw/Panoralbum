/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.nttr.panobumapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private Realm realm;
    List<RowData> albumDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.create_album_btn).setOnClickListener(this);
        realm = Realm.getDefaultInstance(); // DB open

        // test
        RecyclerView rv = (RecyclerView) findViewById(R.id.listRecyclerView);
        RecycleViewAdapter adapter = new RecycleViewAdapter(this.setAlbumData()){
            @Override
            protected void onRecycleViewAdapterClicked(@NonNull RowData version) {
                super.onRecycleViewAdapterClicked(version);
                // Activity 側でタップされたときの処理を行う
                long albumID = version.getDataID();
                Intent intent = new Intent(StartActivity.this, EditAlbumActivity.class);
                intent.putExtra("selectedAlbumID", albumID);
                startActivity(intent);
            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    private List<RowData> setAlbumData(){
        albumDataSet = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Album> albums
                        = realm.where(Album.class).findAll();
                if(albums.size() > 0){
                    for (Album album:
                            albums) {
                        RowData data = new RowData();
                        data.setTitle(album.title);
                        data.setDetail("more 2 photos"); // TODO: show album size
                        data.setID(album.id);
                        Uri rowDataImgUri = Uri.parse(album.images.get(0).uri);
                        try(InputStream stream = getContentResolver().openInputStream(rowDataImgUri)){
                            Bitmap b = BitmapFactory.decodeStream(new BufferedInputStream(stream));
                            data.setBimapt(b);
                        }
                        catch(IOException e){
                        }
                        albumDataSet.add(data);
                    }
                }
            }

        });
        return albumDataSet;
    }

    @Override
    public void onClick(final View v){
        if(v.getId()==R.id.create_album_btn){
            Intent intent = new Intent(StartActivity.this, CreateAlbumActivity.class);
            startActivity(intent);
        }
        else{
        }
    }

    // TODO: uri check

    private void updateView(){
        RecyclerView rv = (RecyclerView) findViewById(R.id.listRecyclerView);
        RecycleViewAdapter adapter = new RecycleViewAdapter(this.setAlbumData()){
            @Override
            protected void onRecycleViewAdapterClicked(@NonNull RowData version) {
                super.onRecycleViewAdapterClicked(version);
                // Activity 側でタップされたときの処理を行う
                long albumID = version.getDataID();
                Intent intent = new Intent(StartActivity.this, EditAlbumActivity.class);
                intent.putExtra("selectedAlbumID", albumID);
                startActivity(intent);
            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

}
