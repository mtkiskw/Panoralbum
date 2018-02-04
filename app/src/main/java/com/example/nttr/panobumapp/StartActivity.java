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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmResults;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.create_album_btn).setOnClickListener(this);
        realm = Realm.getDefaultInstance(); // DB open
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
