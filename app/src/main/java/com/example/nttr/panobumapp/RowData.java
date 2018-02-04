package com.example.nttr.panobumapp;

/**
 * Created by ishikawa on 2018/02/04.
 */

public class RowData {
    private String title;
    private String detail;
    private long dataID;

    public RowData(){
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public void setID(long id){
        this.dataID = id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDetail(){
        return this.detail;
    }

    public long getDataID(){
        return this.dataID;
    }
}
