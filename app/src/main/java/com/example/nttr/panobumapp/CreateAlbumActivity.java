package com.example.nttr.panobumapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CreateAlbumActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_CHOOSE = 23;
    private CreateAlbumActivity.UriAdapter mAdapter;

    // matisse bug
    public static final String EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        findViewById(R.id.open_img_folder_btn).setOnClickListener(this);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new CreateAlbumActivity.UriAdapter());
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
                                    Matisse.from(CreateAlbumActivity.this)
                                            .choose(MimeType.allOf())
                                            .theme(R.style.Matisse_Dracula)
                                            .countable(false)
                                            .maxSelectable(9)
                                            .imageEngine(new GlideEngine())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                    break;
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

            Intent intent = new Intent(getApplication(), AlbumActivity.class);

            ArrayList<Uri> uris = new ArrayList<>(Matisse.obtainResult(data));
            intent.putExtra("selectedUris", uris);
            startActivity(intent);
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