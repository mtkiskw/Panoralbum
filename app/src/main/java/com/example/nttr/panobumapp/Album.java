package com.example.nttr.panobumapp;

import android.net.Uri;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nttr on 2018/01/30.
 */

public class Album extends RealmObject {
    @PrimaryKey
    public long id;
    public String title;
    public RealmList<Image> images;

    public AlbumCover toAlbumCover() {
        return new AlbumCover(title,
                images.size() <= 1  ? "" : "+" + (images.size() - 1),
                id,
                images.isEmpty() ? null : Uri.parse(images.get(0).uri));
    }
}
