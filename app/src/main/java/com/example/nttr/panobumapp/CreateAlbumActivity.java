package com.example.nttr.panobumapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;

public class CreateAlbumActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_CHOOSE = 23;
    private CreateAlbumActivity.UriAdapter mAdapter;

    // matisse bug
    public static final String EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path";

    private Realm realm;
    private Button create;
    private Button read;
    private Button update;
    private Button delete;
    private long currentAlbumId = 0;
    private ArrayList<Uri> currentUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        findViewById(R.id.open_img_folder_btn).setOnClickListener(this);
        findViewById(R.id.start_album_btn).setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new CreateAlbumActivity.UriAdapter());

        final EditText editText = (EditText) findViewById(R.id.edit_title);
        realm = Realm.getDefaultInstance(); // DB open
        create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
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

                        for(int i=0; i<2; i++) {
                            Number maxImage = realm.where(Image.class).max("id");
                            long newImageId = 0;
                            if (maxImage != null) {
                                newImageId = maxImage.longValue() + 1;
                            }

                            String imageURI = "http://hogehoge.com/" + newImageId;
                            Image image = realm.createObject(Image.class, newImageId);
                            image.uri = imageURI;
                            album.images.add(image);
                        }
                        Log.d("Album_", "create" + album.toString());
                    }
                });
            }
        });
        read = (Button) findViewById(R.id.read);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Album album = realm.where(Album.class)
                                .equalTo("id", 0)
                                .findFirst();
                        album.title += "<updated>";
                        Log.d("Album_", album.title);
                    }
                });
            }
        });
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Album album = realm.where(Album.class)
                                .equalTo("id", 0)
                                .findFirst();
                        album.deleteFromRealm();
                        Log.d("Album_", "deleted");
                    }
                });
            }
        });

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

                                            for(int i=0; i<2; i++) {
                                                Number maxImage = realm.where(Image.class).max("id");
                                                long newImageId = 0;
                                                if (maxImage != null) {
                                                    newImageId = maxImage.longValue() + 1;
                                                }

                                                String imageURI = "http://hogehoge.com/" + newImageId;
                                                Image image = realm.createObject(Image.class, newImageId);
                                                image.uri = imageURI;
                                                album.images.add(image);
                                            }
                                            Log.d("Album_", "create" + album.toString());

                                            Matisse.from(CreateAlbumActivity.this)
                                                    .choose(MimeType.allOf())
                                                    .theme(R.style.Matisse_Dracula)
                                                    .countable(false)
                                                    .maxSelectable(9)
                                                    .imageEngine(new GlideEngine())
                                                    .forResult(REQUEST_CODE_CHOOSE);
                                        }
                                    });
                                    // TODO: このActivity消す
                                    break;
                                case R.id.start_album_btn:
                                    if(currentUris != null){
                                        Intent intent = new Intent(getApplication(), AlbumActivity.class);
                                        intent.putExtra("selectedUris", currentUris);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(CreateAlbumActivity.this, R.string.error_empty_album, Toast.LENGTH_LONG);
                                    }
                            }
                        }else {
                            Toast.makeText(CreateAlbumActivity.this, R.string.permission_request_denited, Toast.LENGTH_LONG);
                        }
                        mAdapter.setData(null, null);
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
            mAdapter.setData(Matisse.obtainResult(data), data.getStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH));
            Log.d("Album_", "this album id is" + currentAlbumId);

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

    }

    private static class UriAdapter extends RecyclerView.Adapter<CreateAlbumActivity.UriAdapter.UriViewHolder> {

        private List<Uri> mUris;
        private List<String> mPaths;

        void setData(List<Uri> uris, List<String> paths) {
            mUris = uris;
            mPaths = paths;
            notifyDataSetChanged();
        }

        @Override
        public CreateAlbumActivity.UriAdapter.UriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CreateAlbumActivity.UriAdapter.UriViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.url_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CreateAlbumActivity.UriAdapter.UriViewHolder holder, int position) {
            holder.mUri.setText(mUris.get(position).toString());
//            holder.mPath.setText(mPaths.get(position));

            holder.mUri.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
//            holder.mPath.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
        }

        @Override
        public int getItemCount() {
            return mUris == null ? 0 : mUris.size();
        }

        static class UriViewHolder extends RecyclerView.ViewHolder {

            private TextView mUri;
//            private TextView mPath;

            UriViewHolder(View contentView) {
                super(contentView);
                mUri = (TextView) contentView.findViewById(R.id.uri);
//                mPath = (TextView) contentView.findViewById(R.id.path);
            }
        }
    }
}
