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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
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
                        data.setDetail("more 2 photos");
                        data.setID(album.id);
                        albumDataSet.add(data);
                    }
                    ImageView representativeImage = findViewById(R.id.representative_img);
                    Uri rowDataImgUri = Uri.parse(albums.get(0).images.get(0).toString());
                    Log.d("LOG", rowDataImgUri.toString());
                    try{
                        Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), rowDataImgUri);
                        representativeImage.setImageBitmap(b);
                    }
                    catch (IOException e){
                        Log.d("LOG", "except");

                    }
                }
            }
        });
        return albumDataSet;
    }
    private List<RowData> createDataset() {

        List<RowData> dataset = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            RowData data = new RowData();
            data.setTitle("カサレアル　太郎" + i + "号");
            data.setDetail("カサレアル　太郎は" + i + "個の唐揚げが好き");

            dataset.add(data);
        }
        return dataset;
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

    private void showAlbumList(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Album> albums
                        = realm.where(Album.class).findAll();

                for (Album album:
                        albums) {
                    Log.d("Album_", album.toString());
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

}
