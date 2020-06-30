package com.android.haichun.myvideoplayer.model;

import android.graphics.Bitmap;

public class LocalVideo {
    private String path;
    private Bitmap img;
    private long time;

    public LocalVideo(String path, Bitmap img, long time) {
        this.path = path;
        this.img = img;
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
