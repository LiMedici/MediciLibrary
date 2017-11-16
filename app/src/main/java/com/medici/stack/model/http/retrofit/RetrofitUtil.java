package com.medici.stack.model.http.retrofit;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.medici.stack.model.http.okhttp.OkHttpLogger;
import com.medici.stack.util.gson.DoubleTypeAdapter;
import com.medici.stack.util.gson.IntegerTypeAdapter;
import com.medici.stack.util.gson.LongTypeAdapter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * desc:Retrofit工具类
 * author：李宗好
 * time: 2017/06/14 15:07
 * ic_email_icon：lzh@cnbisoft.com
 */
public class RetrofitUtil {

    private static final long TIMEOUT = 1000;

    private BaseApiService retrofitService = null;

    private RetrofitUtil(){}

    /**
     * 如果你在别的类调用getInstance，就会报错ExceptionInInitializerError。
     * 这是因为类加载时不会为实例变量赋值，对象创建时不会为静态变量赋值。
     * 我们调用getInstance时，此类就开始加载，加载的时候不会为实例变量赋值，
     * 但是会按顺序给静态变量赋值，所以先为example赋值，然后为test赋值即初始化。
     * 但为example赋值时出现了个小插曲，它会调用构造方法创建一个对象。
     * 对象创建时不会为静态变量test赋值，而构造器内却已经调用test，于是报错了。
     */
    private String BASE_URL = "";

    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            /*
            这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
            .addInterceptor(new HttpLoggingInterceptor(new OkHttpLogger())
            .setLevel(HttpLoggingInterceptor.Level.BASIC))
            //Ic9项目需要添加的动态token
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder = chain.request().newBuilder();
                    return chain.proceed(builder.build());
                }
            })
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private BaseApiService makeRetrofitService(){
        if(null != retrofitService){
            return retrofitService;
        }

        return retrofitService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(BaseApiService.class);
    }

    public static BaseApiService getInstance() {
        RetrofitUtil util = new RetrofitUtil();
        return util.makeRetrofitService();
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter(-1))
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(double.class, new DoubleTypeAdapter(-1.00))
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(long.class, new LongTypeAdapter(-1l))
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setLenient()
                .create();
    }

}
