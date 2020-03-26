package com.mim.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface JsonPlaceHolder {
    @GET("weather")
    fun getPosts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String

    ): Call<Post>
}

