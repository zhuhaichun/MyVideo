package com.android.haichun.myvideoplayer.retrofitUtils;

import com.android.haichun.myvideoplayer.model.User;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApi {
    @POST("developerLogin")
    @FormUrlEncoded
    Observable<ResponseBody> regDeve(@Field("name") String name,
                                     @Field("passwd") String password);

    @POST("registerUser")
    @FormUrlEncoded
    Observable<Result<User>> regUser(@Field("apikey") String apiKey,
                                     @Field("name") String name,
                                     @Field("passwd") String password,
                                     @Field("nikeName") String nikeName,
                                     @Field("headerImg") String headerImg,
                                     @Field("phone") String phone,
                                     @Field("email") String email);

    @POST("updateUserInfo")
    @FormUrlEncoded
    Observable<Result<User>> updateUser(@Field("apikey") String apiKey,
                                        @Field("name") String name,
                                        @Field("passwd") String password,
                                        @Field("nikeName") String nikeName,
                                        @Field("headerImg") String headerImg,
                                        @Field("phone") String phone,
                                        @Field("email") String email,
                                        @Field("autograph") String autograph,
                                        @Field("remarks") String remarks);
    @POST("loginUser")
    @FormUrlEncoded
    Observable<Result<User>> login(@Field("apikey") String apiKey,
                                   @Field("name") String name,
                                   @Field("passwd") String password);
}
