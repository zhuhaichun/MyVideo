package com.android.haichun.myvideoplayer.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.adapter.LocalRvAdapter;
import com.android.haichun.myvideoplayer.model.LocalVideo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class LocalActivity extends SwipeBackActivity {

    private static final String TAG = "LocalActivity";
    RecyclerView recyclerView;
    TextView noneTv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_local);
        recyclerView = (RecyclerView) findViewById(R.id.local_rv);
        ImageView exitIv = (ImageView) findViewById(R.id.local_exit_iv);
        noneTv = (TextView) findViewById(R.id.local_none_tv);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LocalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }else {
            setUpRv();
        }
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSwipeBack();
    }

    private void setUpRv(){
        List<LocalVideo> videos = getVideoFromSDCard(this);
        for (LocalVideo video : videos) {
            Log.i(TAG, "onCreate: " + video.getPath());
        }
        if (videos.size() > 0) {
            LocalRvAdapter adapter = new LocalRvAdapter(this, videos);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(adapter);
            noneTv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            noneTv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setUpRv();
            }else{
                Toast.makeText(LocalActivity.this,"权限被拒绝，无法获取本地视频！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<LocalVideo> getVideoFromSDCard(Context context) {
        List<LocalVideo> list = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, null);
        while (cursor.moveToNext()) {
            String path = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            Log.i(TAG, "getVideoFromSDCard: "+path);
            long duration = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            // EntityVideo video = new EntityVideo(path, duration, getVideoThumbnail(path));
            // list.add(video);
            LocalVideo video = new LocalVideo(path,getVideoThumbnail(path),duration);
            list.add(video);
        }
        cursor.close();
        return list;
    }

    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b=null;
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        try {
            mmr.setDataSource(filePath);
            b=mmr.getFrameAtTime(50000,FFmpegMediaMetadataRetriever.OPTION_NEXT_SYNC);
            Log.i(TAG, "getVideoThumbnail: "+ b);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                mmr.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
    private void initSwipeBack(){
        // 可以调用该方法，设置是否允许滑动退出
        setSwipeBackEnable(true);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }



}
