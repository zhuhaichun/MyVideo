package com.android.haichun.myvideoplayer.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.adapter.CollectionRvAdapter;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class HeartActivity extends SwipeBackActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_heart);
        if (MainActivity.mUser == null){
            Toast.makeText(this,"请先登录再查看我的喜欢！",Toast.LENGTH_SHORT).show();
            finish();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.heart_rv);
        TextView noneTv = (TextView) findViewById(R.id.heart_none_tv);
        ImageView exitIv = (ImageView) findViewById(R.id.heart_exit_iv);
        if (MainActivity.mUser.getHearts() != null && MainActivity.mUser.getHearts().size() > 0){
            CollectionRvAdapter adapter = new CollectionRvAdapter(MainActivity.mUser.getHearts(),this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            noneTv.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            noneTv.setVisibility(View.VISIBLE);
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
