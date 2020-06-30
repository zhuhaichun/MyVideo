package com.android.haichun.myvideoplayer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.haichun.myvideoplayer.R;
import com.android.haichun.myvideoplayer.adapter.RvAdapter;
import com.android.haichun.myvideoplayer.model.Video;
import com.android.haichun.myvideoplayer.retrofitUtils.RetrofitRequest;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TodayFragment extends Fragment {

    private static final String TAG = "TodayFragment";
    List<Video.ResultBean> mData;
    RvAdapter mRvAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        mRvAdapter = new RvAdapter(mData,getContext());
        recyclerView.setAdapter(mRvAdapter);
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
        getList();
        return view;
    }

    @SuppressLint("CheckResult")
    private void getList() {
        RetrofitRequest.getToday().getTodayVideoList()
                .filter(todayVideoResult -> !todayVideoResult.isError() && todayVideoResult.response().isSuccessful())
                .map(todayVideoResult -> {
                    assert todayVideoResult.response() != null;
                    assert todayVideoResult.response().body() != null;
                    return todayVideoResult.response().body().getResult();
                })
                .flatMap((Function<List<Video.ResultBean>, ObservableSource<Video.ResultBean>>) resultBeans -> Observable.fromIterable(resultBeans))
                .filter(resultBean -> {
                    String type = resultBean.getType();
                    return "followCard".equals(type);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBean -> mData.add(resultBean),
                        Throwable::printStackTrace,
                        () -> {
                    mRvAdapter.notifyDataSetChanged();
                });
    }
}
