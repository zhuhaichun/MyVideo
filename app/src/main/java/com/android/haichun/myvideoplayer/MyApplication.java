package com.android.haichun.myvideoplayer;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

public class MyApplication extends Application {

    private static MyApplication myApplication;
    public static MyApplication getInstance(){
        return myApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }
}
