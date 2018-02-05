package com.example.nttr.panobumapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;

public class EditAlbumActivity extends AppCompatActivity implements View.OnClickListener{
    private Realm realm;
    private long selectedAlbumID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        Intent intent = getIntent();
        selectedAlbumID = intent.getLongExtra("selectedAlbumID", 0);
        realm = Realm.getDefaultInstance(); // DB open
        setAlbumData(selectedAlbumID);

        findViewById(R.id.start_album_btn).setOnClickListener(this);
    }

    private void setAlbumData(final long albumID){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Album album = realm.where(Album.class)
                        .equalTo("id", albumID)
                        .findFirst();
                TextView titleView = findViewById(R.id.selected_album_title);
                titleView.setText(album.title);
                TextView urisView = findViewById(R.id.uri_for_debug);
                String uriText = "";
                for (Image image:
                     album.images) {
                    uriText += image.uri;
                    urisView.setText(uriText);
                }
            }
        });
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

    @Override
    public void onClick(final View v){
        switch (v.getId()){
            case R.id.start_album_btn:
                Intent intent = new Intent(getApplication(), AlbumActivity.class);
                ArrayList<Uri> selectedUris = getAlbumUris(selectedAlbumID);
                intent.putExtra("selectedUris", selectedUris);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}
