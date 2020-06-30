package com.android.haichun.myvideoplayer.retrofitUtils;

import com.android.haichun.myvideoplayer.model.RecommendVideo;
import com.android.haichun.myvideoplayer.model.Video;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoListApi {
    @GET("todayVideo")
    Observable<Result<Video>> getTodayVideoList();
    @GET("videoCategoryDetails")
    Observable<Result<Video>> getCategoryVideo(@Query("id") int id);
    @GET("videoRecommend")
    Observable<Result<RecommendVideo>> getRecommendVideo(@Query("id") int id);
}
