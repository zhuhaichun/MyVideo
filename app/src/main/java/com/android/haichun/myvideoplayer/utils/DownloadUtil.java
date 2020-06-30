package com.android.haichun.myvideoplayer.utils;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;

import androidx.annotation.RequiresApi;

public class DownloadUtil {
    public int singleTaskId = 0;
    public String mSinglePath;
    public String mSaveFolder = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_MOVIES;
    public int notificationId;
    BaseDownloadTask singleTask;
    private String videoUrl;
    private String singleFileSaveName;

    public DownloadUtil(int notificationId, String name, String videoUrl) {
        this.notificationId = notificationId;
        this.videoUrl = videoUrl;
        singleFileSaveName = name + ".mp4";
        mSinglePath = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_MOVIES
                + File.separator + singleFileSaveName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void start_single() {
        String url = videoUrl;
        singleTask = FileDownloader.getImpl().create(url)
//                .setPath(mSinglePath,false)
                .setPath(mSinglePath, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                //.setTag()
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("DownloadUtil", "pending taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);
                        NotificationUtil.showNotification("下载", "等待下载", notificationId, "11", 0, 100);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("DownloadUtil", "progress taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes + ",speed:" + task.getSpeed());
                        int progress = (int) ((double) soFarBytes / totalBytes * 100);
                        NotificationUtil.updateProgress(notificationId, progress);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.d("DownloadUtil", "blockComplete taskId:" + task.getId() + ",filePath:" + task.getPath() + ",fileName:" + task.getFilename() + ",speed:" + task.getSpeed() + ",isReuse:" + task.reuse());
                        NotificationUtil.completeProgress(notificationId);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d("DownloadUtil", "completed taskId:" + task.getId() + ",isReuse:" + task.reuse());
                        NotificationUtil.completeProgress(notificationId);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("DownloadUtil", "paused taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);
                        int progress = (int) ((double) soFarBytes / totalBytes * 100);
                        NotificationUtil.pauseProgress(notificationId, progress);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d("DownloadUtil", "error taskId:" + task.getId() + ",e:" + e.getLocalizedMessage());
                        NotificationUtil.errorProgress(notificationId);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.d("DownloadUtil", "warn taskId:" + task.getId());
                    }
                });
        singleTaskId = singleTask.start();
    }

    public void pause_single() {
        Log.d("DownloadUtil", "pause_single task:" + singleTaskId);
        FileDownloader.getImpl().pause(singleTaskId);
    }

    public void delete_single() {
        //删除单个任务的database记录
        boolean deleteData = FileDownloader.getImpl().clear(singleTaskId, mSaveFolder);
        File targetFile = new File(mSinglePath);
        boolean delate = false;
        if (targetFile.exists()) {
            delate = targetFile.delete();
        }

        Log.d("DownloadUtil", "delete_single file,deleteDataBase:" + deleteData + ",mSinglePath:" + mSinglePath + ",delate:" + delate);

        new File(FileDownloadUtils.getTempPath(mSinglePath)).delete();
    }
}
