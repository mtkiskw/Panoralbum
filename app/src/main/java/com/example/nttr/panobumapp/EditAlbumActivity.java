package com.example.nttr.panobumapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;

public class EditAlbumActivity extends AppCompatActivity implements View.OnClickListener{
    private Realm realm;
    private long selectedAlbumID = 0;
    List<AlbumRowData> albumDataSet;
    private static final int REQUEST_CODE_CHOOSE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        Intent intent = getIntent();
        selectedAlbumID = intent.getLongExtra("selectedAlbumID", 0);
        realm = Realm.getDefaultInstance(); // DB open

        findViewById(R.id.start_album_btn).setOnClickListener(this);
        findViewById(R.id.add_img_btn).setOnClickListener(this);
        final RecyclerView rv = (RecyclerView) findViewById(R.id.listRecyclerView);

        AlbumRecycleViewAdapter adapter = new AlbumRecycleViewAdapter(this.setAlbumData(selectedAlbumID)){
            @Override
            protected void onAlbumRecycleViewAdapterClicked(@NonNull AlbumRowData version) {
                super.onAlbumRecycleViewAdapterClicked(version);
                // Activity 側でタップされたときの処理を行う
//                long albumID = version.getDataID();
//                Intent intent = new Intent(EditAlbumActivity.this, EditAlbumActivity.class);
//                intent.putExtra("selectedAlbumID", albumID);
//                startActivity(intent);
            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

    }

    private List<AlbumRowData> setAlbumData(final long albumID){
        albumDataSet = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Album album = realm.where(Album.class)
                        .equalTo("id", albumID)
                        .findFirst();
                TextView titleView = findViewById(R.id.selected_album_title);
                titleView.setText(album.title);

                for (Image image:
                     album.images) {
                    AlbumRowData data = new AlbumRowData();
                    Uri rowDataImgUri = Uri.parse(image.uri);
                    try(InputStream stream = getContentResolver().openInputStream(rowDataImgUri)){
                        Bitmap b = BitmapFactory.decodeStream(new BufferedInputStream(stream));
                        data.setBimap(b);
                    }
                    catch(IOException e){
                    }
                    albumDataSet.add(data);
                }
            }

        });
        return albumDataSet;
    }

    private ArrayList<Uri> getAlbumUris(final long albumID){
        final ArrayList<Uri> uris = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Album album = realm.where(Album.class)
                        .equalTo("id", albumID)
                        .findFirst();
                for (Image image:
                        album.images) {
                    uris.add(Uri.parse(image.uri));
                }
            }
        });
        return uris;
    }

    // TODO: update
    // TODO: add image
    // TODO: delete image
    // TODO: uri check

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
                        }else {
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
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Album album = realm.where(Album.class)
                            .equalTo("id", selectedAlbumID)
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
        updateView();
    }

    private void updateView(){
        RecyclerView rv = (RecyclerView) findViewById(R.id.listRecyclerView);
        AlbumRecycleViewAdapter adapter = new AlbumRecycleViewAdapter(this.setAlbumData(selectedAlbumID)){
            @Override
            protected void onAlbumRecycleViewAdapterClicked(@NonNull AlbumRowData version){
                super.onAlbumRecycleViewAdapterClicked(version);
            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}
