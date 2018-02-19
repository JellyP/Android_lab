package com.example.administrator.lab9.service;

import com.example.administrator.lab9.model.Github;
import com.example.administrator.lab9.model.Repos;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 用于调用的接口
 */

public interface GithubService {
    @GET("users/{user}")
    Observable<Github> getUser(@Path("user")String user);//被订阅者

    @GET("/users/{user}/repos")
    Observable<List<Repos>> getRepos(@Path("user")String user);
}
