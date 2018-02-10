package com.example.nttr.panobumapp;

import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ishikawa on 2018/02/04.
 */

public class AlbumCover {

    private String title;

    private String detail;

    private long dataID;

    @Nullable
    private Uri coverImageUri;

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public long getDataID() {
        return dataID;
    }

    @Nullable
    public Uri getCoverImageUri() {
        return coverImageUri;
    }

    public AlbumCover(final String title, final String detail, final long dataID, @Nullable final Uri coverImageUri) {
        this.title = title;
        this.detail = detail;
        this.dataID = dataID;
        this.coverImageUri = coverImageUri;
    }
}
