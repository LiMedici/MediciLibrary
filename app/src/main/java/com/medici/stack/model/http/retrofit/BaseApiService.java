package com.medici.stack.model.http.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * desc: Retrofit 基础的一些接口
 * author：李宗好
 * time: 2017/6/2 11:12
 * ic_email_icon：lzh@cnbisoft.com
 */
public interface BaseApiService {

    @GET("{url}")
    Observable<Result<ResponseBody>> httpGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);

    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> httpPost(
            @Path("url") String url,
            @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @Multipart
    @POST("{url}")
    Observable<ResponseBody> uploadFile(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @Multipart
    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Part("description") RequestBody description,
            @PartMap() Map<String, RequestBody> maps);

    @FormUrlEncoded
    @POST("{url}")
    Call<ResponseBody> httpPost(
            @Path("url") String url,
            @Body Object object);



}
