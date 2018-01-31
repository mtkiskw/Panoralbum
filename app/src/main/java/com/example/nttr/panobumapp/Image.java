package com.example.nttr.panobumapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nttr on 2018/01/31.
 */

public class Image extends RealmObject {
    @PrimaryKey
    public long id;
    public String uri;
}
