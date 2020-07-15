package com.example.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;

public class NoteModel {
    private int id;
    private String title;
    private String subtitle;
    private String bitmapPath;
    private String thumbNailPath;
    private boolean type;


    public NoteModel(String title,String subtitle,boolean type,int id) {
        this.title = title;
        this.subtitle=subtitle;
        this.type=type;
        this.id=id;
    }

    public NoteModel(String title, String bitmapPath,String thumbNailPath, boolean type,int id ) {
        this.title = title;
        this.bitmapPath=bitmapPath;
        this.thumbNailPath = thumbNailPath;
        this.type = type;
        this.id=id;


    }

    public String getBitmapPath() {
        return bitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        this.bitmapPath = bitmapPath;
    }

    public String getThumbNailPath() {
        return thumbNailPath;
    }

    public void setThumbNailPath(String thumbNailPath) {
        this.thumbNailPath = thumbNailPath;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
