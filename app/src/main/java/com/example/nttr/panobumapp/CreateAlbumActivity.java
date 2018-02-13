package com.example.nttr.panobumapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nttr.panobumapp.util.RealmUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;

import io.realm.Realm;

public class CreateAlbumActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;

    private Realm realm;
    private ArrayList<Uri> currentUris;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);

        findViewById(R.id.open_img_folder_btn)
                .setOnClickListener(this::onClickOpenImageFolderButton);

        editText = findViewById(R.id.edit_title);
        realm = Realm.getDefaultInstance(); // DB open
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void onClickOpenImageFolderButton(final View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permitted -> {
                    if (!permitted) {
                        Toast.makeText(this, R.string.permission_request_denited, Toast.LENGTH_LONG).show();
                        return;
                    }
                    realm.executeTransaction(realm -> {

                        String albumTitle = editText.getText().toString();
                        if (TextUtils.isEmpty(albumTitle)) {
                            editText.setError("The Album name cannot be empty");
                            return;
                        }

                        Matisse.from(CreateAlbumActivity.this)
                                .choose(MimeType.allOf())
                                .theme(R.style.Matisse_Dracula)
                                .countable(false)
                                .maxSelectable(9)
                                .imageEngine(new GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE);
                    });
                }, e -> {
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE:
                if (resultCode == RESULT_OK) {
                    saveAlbumAndLaunch(data);
                } else {
                    Toast.makeText(this, R.string.label_select_pics, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveAlbumAndLaunch(Intent data) {
        currentUris = new ArrayList<>(Matisse.obtainResult(data));
        realm.executeTransaction(realm -> {
            long newAlbumId = RealmUtils.getNewPrimaryId(realm, Album.class);
            Album album = realm.createObject(Album.class, newAlbumId);
            album.title = editText.getText().toString();

            for (Uri uri : currentUris) {
                String imageUri = uri.toString();

                long newImageId = RealmUtils.getNewPrimaryId(realm, Image.class);
                Image image = realm.createObject(Image.class, newImageId);
                image.uri = imageUri;
                album.images.add(image);
            }
            Intent intent = new Intent(CreateAlbumActivity.this, EditAlbumActivity.class);
            intent.putExtra("selectedAlbumID", album.id);
            startActivity(intent);
            finish();
        });
    }
}
