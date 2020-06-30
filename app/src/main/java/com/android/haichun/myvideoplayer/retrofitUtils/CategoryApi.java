package com.android.haichun.myvideoplayer.retrofitUtils;

import com.android.haichun.myvideoplayer.model.Category;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("videoCategory")
    Observable<Result<Category>> getCategory();
}
