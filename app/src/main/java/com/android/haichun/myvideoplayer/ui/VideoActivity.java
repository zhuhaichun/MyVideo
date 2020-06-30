package com.android.haichun.myvideoplayer.ui;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.adapter.VideoRvAdapter;
import com.android.haichun.myvideoplayer.model.RecommendVideo;
import com.android.haichun.myvideoplayer.model.Video;
import com.android.haichun.myvideoplayer.retrofitUtils.RetrofitRequest;
import com.android.haichun.myvideoplayer.retrofitUtils.VideoListApi;
import com.android.haichun.myvideoplayer.utils.DownloadUtil;
import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import retrofit2.adapter.rxjava2.Result;

public class VideoActivity extends SwipeBackActivity {

    private static final String TAG = "VideoActivity";
    VideoRvAdapter mRvAdapter;
    List<RecommendVideo.ResultBean> mBeans;
    RecyclerView recyclerView;
    Boolean isCollection = false;
    Boolean isHeart = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_video);
        Video.ResultBean.DataBeanX.ContentBean.DataBean videoBean = (Video.ResultBean.DataBeanX.ContentBean.DataBean) getIntent().getSerializableExtra("video");
        long playPosition = getIntent().getLongExtra("playPosition", 0);
        recyclerView = (RecyclerView) findViewById(R.id.video_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CustomJzvdStd jzvdStd = (CustomJzvdStd) findViewById(R.id.video_jzvd_std);
        mBeans = new ArrayList<>();
        mRvAdapter = new VideoRvAdapter(mBeans, this);
        recyclerView.setAdapter(mRvAdapter);
        if (videoBean != null) {
            setHeader(videoBean);
            getRecommendList(videoBean.getId());
            jzvdStd.setUp(videoBean.getPlayUrl(), "", Jzvd.SCREEN_NORMAL);
            if (playPosition != 0) {
                jzvdStd.seekToInAdvance = playPosition;
                jzvdStd.startButton.performClick();
            }
            Glide.with(this).load(videoBean.getCover().getFeed()).into(jzvdStd.posterImageView);
        }
        initSwipeBack();

    }

    private void setHeader(Video.ResultBean.DataBeanX.ContentBean.DataBean videoBean) {
        View view = getLayoutInflater().inflate(R.layout.video_recycler_header, recyclerView, false);
        TextView headTitle = view.findViewById(R.id.header_vd_title);
        TextView headCategory = view.findViewById(R.id.header_vd_category);
        ExpandableTextView desc = view.findViewById(R.id.header_vd_desc);
        TextView heart = view.findViewById(R.id.header_vd_heart);
        TextView share = view.findViewById(R.id.header_vd_share);
        TextView collection = view.findViewById(R.id.header_vd_collection);
        TextView downloanTv = view.findViewById(R.id.header_vd_download);
        headTitle.setText(videoBean.getTitle());
        headCategory.setText(videoBean.getCategory() + " / " + videoBean.getAuthor().getName());
        desc.setText(videoBean.getDescription());
        heart.setText(videoBean.getConsumption().getCollectionCount() + "");
        share.setText(videoBean.getConsumption().getShareCount() + "");
        collection.setText(videoBean.getConsumption().getRealCollectionCount() + "");
        if (MainActivity.mUser != null) {
            if (MainActivity.mUser.getCollection() != null) {
                for (Video.ResultBean.DataBeanX.ContentBean.DataBean dataBean : MainActivity.mUser.getCollection()) {
                    if (dataBean.getId() == videoBean.getId()) {
                        isCollection = true;
                        Drawable drawable = getDrawable(R.drawable.star_fill);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        collection.setCompoundDrawables(drawable, null, null, null);
                        break;
                    }
                }
            } else {
                MainActivity.mUser.setCollection(new ArrayList<>());
            }
            if (MainActivity.mUser.getHearts() != null) {
                for (Video.ResultBean.DataBeanX.ContentBean.DataBean dataBean : MainActivity.mUser.getHearts()) {
                    if (dataBean.getId() == videoBean.getId()) {
                        isHeart = true;
                        Drawable drawable = getDrawable(R.drawable.heart_fill);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        heart.setCompoundDrawables(drawable, null, null, null);
                        break;
                    }
                }
            } else {
                MainActivity.mUser.setHearts(new ArrayList<>());
            }
        }
        collection.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (MainActivity.mUser != null) {
                    if (!isCollection) {
                        MainActivity.mUser.getCollection().add(videoBean);
                        isCollection = true;
                        Drawable drawable = getDrawable(R.drawable.star_fill);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        collection.setCompoundDrawables(drawable, null, null, null);
                        Toast.makeText(VideoActivity.this, "收藏成功，请在我的收藏查看！", Toast.LENGTH_SHORT).show();
                    } else {
                        MainActivity.mUser.getCollection().removeIf(new java.util.function.Predicate<Video.ResultBean.DataBeanX.ContentBean.DataBean>() {
                            @Override
                            public boolean test(Video.ResultBean.DataBeanX.ContentBean.DataBean dataBean) {
                                return dataBean.getId() == videoBean.getId();
                            }
                        });
                        isCollection = false;
                        Drawable drawable = getDrawable(R.drawable.star);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        collection.setCompoundDrawables(drawable, null, null, null);
                        Toast.makeText(VideoActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VideoActivity.this, "请先登录，再收藏！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (MainActivity.mUser != null) {
                    if (isHeart) {
                        MainActivity.mUser.getHearts().removeIf(new java.util.function.Predicate<Video.ResultBean.DataBeanX.ContentBean.DataBean>() {
                            @Override
                            public boolean test(Video.ResultBean.DataBeanX.ContentBean.DataBean dataBean) {
                                return dataBean.getId() == videoBean.getId();
                            }
                        });
                        isHeart = false;
                        Drawable drawable = getDrawable(R.drawable.heart);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        heart.setCompoundDrawables(drawable, null, null, null);
                        Toast.makeText(VideoActivity.this, "取消喜欢成功", Toast.LENGTH_SHORT).show();
                    } else {
                        MainActivity.mUser.getHearts().add(videoBean);
                        isHeart = true;
                        Drawable drawable = getDrawable(R.drawable.heart_fill);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        heart.setCompoundDrawables(drawable, null, null, null);
                        Toast.makeText(VideoActivity.this, "成功添加到我的喜欢", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VideoActivity.this, "请先登录，再添加到我喜欢！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        downloanTv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (MainActivity.mUser != null) {
                    DownloadUtil downloadUtil = new DownloadUtil(videoBean.getId(), videoBean.getId() + "", videoBean.getPlayUrl());
                    downloadUtil.start_single();
                    Toast.makeText(VideoActivity.this, "正在下载视频到本地", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VideoActivity.this, "请先登录再下载视频！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRvAdapter.setHeaderView(view);
    }

    @SuppressLint("CheckResult")
    private void getRecommendList(int id) {
        RetrofitRequest.getRetrofit().create(VideoListApi.class)
                .getRecommendVideo(id)
                .filter(new Predicate<Result<RecommendVideo>>() {
                    @Override
                    public boolean test(Result<RecommendVideo> videoResult) throws Exception {
                        return !videoResult.isError() && videoResult.response().isSuccessful();
                    }
                })
                .map(new Function<Result<RecommendVideo>, List<RecommendVideo.ResultBean>>() {
                    @Override
                    public List<RecommendVideo.ResultBean> apply(Result<RecommendVideo> videoResult) throws Exception {
                        return videoResult.response().body().getResult();
                    }
                })
                .flatMap(new Function<List<RecommendVideo.ResultBean>, ObservableSource<RecommendVideo.ResultBean>>() {
                    @Override
                    public ObservableSource<RecommendVideo.ResultBean> apply(List<RecommendVideo.ResultBean> resultBeans) throws Exception {
                        return Observable.fromIterable(resultBeans);
                    }
                })
                .filter(new Predicate<RecommendVideo.ResultBean>() {
                    @Override
                    public boolean test(RecommendVideo.ResultBean resultBean) throws Exception {
                        return "videoSmallCard".equals(resultBean.getType());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RecommendVideo.ResultBean>() {
                    @Override
                    public void accept(RecommendVideo.ResultBean resultBean) throws Exception {
                        mBeans.add(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        for (RecommendVideo.ResultBean bean : mBeans) {
                            Log.i(TAG, "accept: " + bean.getType());
                        }
                        mRvAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initSwipeBack() {
        // 可以调用该方法，设置是否允许滑动退出
        setSwipeBackEnable(true);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }
    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        Jzvd.releaseAllVideos();
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Jzvd.releaseAllVideos();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Jzvd.goOnPlayOnResume();
    }
}
