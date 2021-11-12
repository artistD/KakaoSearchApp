package com.will_d.tp11kakaoplacesearchapp

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

public interface RetrofitService {

    @Headers("Authorization: KakaoAK 2e457af7655426174e3d5eae4bd79b18")
    @GET("v2/local/search/keyword.json")
    fun searchPlaceByString(@Query("query")query :String, @Query("x")longitude:String, @Query("y")latitude:String) : retrofit2.Call<String>
//    내가써야할 수천줄의 코드를 Call객체가 가지고 있음

    @Headers("Authorization: KakaoAK 2e457af7655426174e3d5eae4bd79b18")
    @GET("v2/local/search/keyword.json")
    fun searchPlace(@Query("query")query :String, @Query("x")longitude:String, @Query("y")latitude:String) : retrofit2.Call<SearchLocalApiResponse>
}