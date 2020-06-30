package com.android.haichun.myvideoplayer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.adapter.RvAdapter;
import com.android.haichun.myvideoplayer.model.Video;
import com.android.haichun.myvideoplayer.retrofitUtils.RetrofitRequest;
import com.android.haichun.myvideoplayer.retrofitUtils.VideoListApi;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CategoryListFragment extends Fragment {

    int categoryId;
    List<Video.ResultBean> mData;
    RvAdapter mRvAdapter;

    public CategoryListFragment(int categoryId) {
        this.categoryId = categoryId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        mRvAdapter = new RvAdapter(mData,getContext());
        recyclerView.setAdapter(mRvAdapter);
        getList();
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) { }
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                Jzvd jzvd = view.findViewById(R.id.item_jzvd_std);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });
        return view;
    }

    @SuppressLint("CheckResult")
    private void getList(){
        RetrofitRequest.getRetrofit().create(VideoListApi.class)
                .getCategoryVideo(categoryId)
                .filter(todayVideoResult -> !todayVideoResult.isError() && todayVideoResult.response().isSuccessful())
                .map(todayVideoResult -> todayVideoResult.response().body().getResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {
                    mData.clear();
                    mData.addAll(resultBeans);
                    for(Video.ResultBean bean : mData){
                        Log.i("Category", "accept: "+bean.getData().getContent().getData().getTitle());
                    }
                    mRvAdapter.notifyDataSetChanged();
                });
    }
}
