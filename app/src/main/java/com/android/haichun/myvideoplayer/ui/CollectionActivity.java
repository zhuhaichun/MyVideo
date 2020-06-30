package com.android.haichun.myvideoplayer.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.adapter.CollectionRvAdapter;
import com.android.haichun.myvideoplayer.model.Video;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CollectionActivity extends SwipeBackActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_collection);
        if (MainActivity.mUser == null){
            Toast.makeText(this,"请先登录再查看收藏",Toast.LENGTH_SHORT).show();
            finish();
        }
        List<Video.ResultBean.DataBeanX.ContentBean.DataBean> collections = MainActivity.mUser.getCollection();
        ImageView exitIv = (ImageView) findViewById(R.id.collection_exit_iv);
        TextView noneTv = (TextView) findViewById(R.id.collection_none_tv);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.collection_rv);
        if (collections != null && collections.size() > 0){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            CollectionRvAdapter adapter = new CollectionRvAdapter(collections,this);
            recyclerView.setAdapter(adapter);
            noneTv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            noneTv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSwipeBack();
    }
    private void initSwipeBack(){
        // 可以调用该方法，设置是否允许滑动退出
        setSwipeBackEnable(true);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }
}
