package com.example.nttr.panobumapp;

import android.graphics.Bitmap;

/**
 * Created by nttr on 2018/02/06.
 */

public class AlbumRowData {
    private long dataID;
    private Bitmap bitmap;

    public AlbumRowData(){
    }

    public void setID(long id){
        this.dataID = id;
    }

    public void setBimap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public long getDataID(){
        return this.dataID;
    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }

}
