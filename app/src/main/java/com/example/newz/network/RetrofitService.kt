package com.example.newz.network

import com.example.newz.model.Headline
import com.example.newz.model.News
import com.example.newz.model.NewsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface  RetrofitService {
    //api key = 87bb7a661afb4c2a8c962e6e0d8381a2
//    @GET("top-headlines?country=in&category={category}&apiKey=87bb7a661afb4c2a8c962e6e0d8381a2")
//    fun getDataFromAPI(@Path("category") category : String) : Call<NewsList>
    @GET("top-headlines")
    fun getDataFromApi(
        @Query("country") country : String,
        @Query("category") category : String,
        @Query("apiKey") apiKey : String
    ) : Call<Headline>
}