package com.example.nttr.panobumapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;

public class CreateAlbumActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_CHOOSE = 23;

    private Realm realm;
    private long currentAlbumId = 0;
    private ArrayList<Uri> currentUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);

        findViewById(R.id.open_img_folder_btn).setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final EditText editText = (EditText) findViewById(R.id.edit_title);
        realm = Realm.getDefaultInstance(); // DB open
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onClick(final View v){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            switch (v.getId()){
                                case R.id.open_img_folder_btn:
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            final EditText editText = (EditText) findViewById(R.id.edit_title);

                                            Number max = realm.where(Album.class).max("id");
                                            long newId = 0;
                                            if(max != null){
                                                newId = max.longValue() + 1;
                                            }
                                            Album album =
                                                    realm.createObject(Album.class, newId);

                                            String albumTitle = editText.getText().toString();
                                            if(TextUtils.isEmpty(albumTitle)) {
                                                editText.setError("The Album name cannot be empty");
                                                return;
                                            }

                                            album.title = editText.getText().toString();
                                            currentAlbumId = newId;

                                            Matisse.from(CreateAlbumActivity.this)
                                                    .choose(MimeType.allOf())
                                                    .theme(R.style.Matisse_Dracula)
                                                    .countable(false)
                                                    .maxSelectable(9)
                                                    .imageEngine(new GlideEngine())
                                                    .forResult(REQUEST_CODE_CHOOSE);
                                        }
                                    });
                                    break;
                            }
                        }else {
                            Toast.makeText(CreateAlbumActivity.this, R.string.permission_request_denited, Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            currentUris = new ArrayList<>(Matisse.obtainResult(data));
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Album album = realm.where(Album.class)
                            .equalTo("id", currentAlbumId)
                            .findFirst();
                    for (Uri uri:
                            currentUris) {
                        String imageUri = uri.toString();

                        Number maxImage = realm.where(Image.class).max("id");
                        long newImageId = 0;
                        if (maxImage != null) {
                            newImageId = maxImage.longValue() + 1;
                        }

                        Image image = realm.createObject(Image.class, newImageId);
                        image.uri = imageUri;
                        album.images.add(image);
                    }
                }
            });
        }
        Intent intent = new Intent(CreateAlbumActivity.this, EditAlbumActivity.class);
        intent.putExtra("selectedAlbumID", currentAlbumId);
        startActivity(intent);

        finish(); // finish activity
    }
}
