package com.example.administrator.meetingtaste.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/12/21.
 */

public class DBFactory
{
    final public static String BASE_URL = "http://172.18.93.202:8080/Android_proj/";

    /**
     * 创建OkHttp实例
     * @return
     */
    private static OkHttpClient createOkHttp()
    {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();
    }

    /**
     * 创建Retrofit
     * @param url
     * @return
     */
    private static Retrofit createRetrofit(String url)
    {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }

    /**
     * 创建服务
     * @return
     */
    public static DBService createDBService()
    {
        Retrofit retrofit = createRetrofit(BASE_URL);
        DBService service = retrofit.create(DBService.class);
        return service;
    }
}
