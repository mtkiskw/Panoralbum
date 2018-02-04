package com.example.nttr.panobumapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.realm.Realm;

public class EditAlbumActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        Intent intent = getIntent();
        long albumID = intent.getLongExtra("selectedAlbumID", 0);
        realm = Realm.getDefaultInstance(); // DB open
        setAlbumData(albumID);
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

//                album.title += "<updated>";
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}
