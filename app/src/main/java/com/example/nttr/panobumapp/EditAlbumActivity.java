package com.example.nttr.panobumapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;

public class EditAlbumActivity extends AppCompatActivity implements View.OnClickListener {
    private Realm realm;
    private long selectedAlbumID = 0;
    private static final int REQUEST_CODE_CHOOSE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        Intent intent = getIntent();
        selectedAlbumID = intent.getLongExtra("selectedAlbumID", 0);
        realm = Realm.getDefaultInstance(); // DB open

        TextView titleView = (TextView) findViewById(R.id.selected_album_title);
        findViewById(R.id.start_album_btn).setOnClickListener(this);
        findViewById(R.id.add_img_btn).setOnClickListener(this);
        final RecyclerView rv = findViewById(R.id.listRecyclerView);

        Album album = getAlbum(selectedAlbumID);
        if (album == null) {
            Toast.makeText(this, R.string.cannot_find_album, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        titleView.setText(album.title);

        removeLostImageUri(album);
        AlbumRecyclerViewAdapter adapter = new AlbumRecyclerViewAdapter(album.images, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    private void removeLostImageUri(Album album){
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < album.images.size(); i++) {
            Image image = album.images.get(i);
            Uri uri = Uri.parse(image.uri);
            ContentResolver cr = getContentResolver();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cur = cr.query(uri, projection, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String filePath = cur.getString(0);
                    if (new File(filePath).exists()) {
                        continue;
                    }
                }
                cur.close();
            }
            images.add(image);
        }
        if(images.size() == album.images.size()){
            Toast.makeText(this, R.string.add_photo_to_album, Toast.LENGTH_LONG).show();
            FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.start_album_btn);
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        realm.executeTransaction(realm -> {
            for (final Image image : images) {
                image.deleteFromRealm();
            }
        });
    }

    @Nullable
    private Album getAlbum(final long albumID) {
        return realm.where(Album.class)
                .equalTo("id", albumID)
                .findFirst();
    }

    private ArrayList<Uri> getAlbumUris(final long albumID) {
        final ArrayList<Uri> uris = new ArrayList<>();
        realm.executeTransaction(realm -> {
            Album album = realm.where(Album.class)
                    .equalTo("id", albumID)
                    .findFirst();
            for (Image image : album.images) {
                uris.add(Uri.parse(image.uri));
            }
        });
        return uris;
    }
    // TODO: delete image

    @Override
    public void onClick(final View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            switch (v.getId()) {
                                case R.id.start_album_btn:
                                    Intent intent = new Intent(getApplication(), AlbumActivity.class);
                                    final ArrayList<Uri> selectedUris = getAlbumUris(selectedAlbumID);
                                    intent.putExtra("selectedUris", selectedUris);
                                    startActivity(intent);
                                    break;

                                case R.id.add_img_btn:
                                    Log.d("LOG", "pushed");
                                    Matisse.from(EditAlbumActivity.this)
                                            .choose(MimeType.allOf())
                                            .theme(R.style.Matisse_Dracula)
                                            .countable(false)
                                            .maxSelectable(9)
                                            .imageEngine(new GlideEngine())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                    break;
                            }
                        } else {
                            Toast.makeText(EditAlbumActivity.this, R.string.permission_request_denited, Toast.LENGTH_LONG);
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
            final ArrayList<Uri> currentUris = new ArrayList<>(Matisse.obtainResult(data));
            realm.executeTransaction(realm -> {
                Album album = realm.where(Album.class)
                        .equalTo("id", selectedAlbumID)
                        .findFirst();
                for (Uri uri :
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
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
