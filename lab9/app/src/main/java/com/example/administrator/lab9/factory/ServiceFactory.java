package com.example.administrator.lab9.factory;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络服务
 */
public class ServiceFactory {
    public static OkHttpClient createOkHttp()
    {
        OkHttpClient okHttpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                                  .readTimeout(30,TimeUnit.SECONDS)
                                  .writeTimeout(10,TimeUnit.SECONDS)
                                  .build();
        return okHttpClient;
    }
    public static Retrofit createRetrofit(String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())//添加Gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//异步调用
                .client(createOkHttp())
                .build();
    }

}

