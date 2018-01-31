package com.example.nttr.panobumapp;

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
}
