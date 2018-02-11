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

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;

public class StartActivity extends AppCompatActivity {
    private Realm realm;
    private AlbumCoverAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.create_album_btn).setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, CreateAlbumActivity.class);
            startActivity(intent);
        });
        realm = Realm.getDefaultInstance(); // DB open

        RecyclerView rv = findViewById(R.id.listRecyclerView);
        realm.executeTransaction(realm ->
                adapter = new AlbumCoverAdapter(realm.where(Album.class).findAll(), this));
        adapter.setOnItemClickListener((albumCover, viewHolder) -> {
            long albumID = albumCover.getDataID();
            Intent intent = new Intent(StartActivity.this, EditAlbumActivity.class);
            intent.putExtra("selectedAlbumID", albumID);
            startActivity(intent);
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

//        checkView();
    }

    // TODO: uri check
    private void checkView() {
        realm.executeTransaction(realm -> {
            RealmResults<Album> albums = realm.where(Album.class).findAll();
            if (albums.size() <= 0)
                return;

            for (Album album : albums) {
                for (Image image : album.images) {
                    Uri targetImgUri = Uri.parse(image.uri);
                    ContentResolver cr = getContentResolver();
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    Cursor cur = cr.query(targetImgUri, projection, null, null, null);
                    if (cur != null) {
                        if (cur.moveToFirst()) {
                            String filePath = cur.getString(0);
                            if (new File(filePath).exists()) {
                                // do something if it exists
                            } else {
                                // File was not found
                                Log.d("LOG", filePath);
                                // TODO: bitmap返ってこなかったら、で判断？
                                image.deleteFromRealm();
                            }
                        } else {
                            // Uri was ok but no entry found.
                            Log.d("LOG", "here");

                            image.deleteFromRealm();
                            // TODO: 表紙になってなくてもdeleteするとエラーになる
                        }
                        cur.close();
                    } else {
                        // content Uri was invalid or some other error occurred
//                                image.deleteFromRealm();
                    }
                }
                if (album.images.size() == 0) {
                    album.deleteFromRealm();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
