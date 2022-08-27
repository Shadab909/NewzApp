package com.example.newz.network.source

import com.example.newz.model.Headline
import com.example.newz.model.Source
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SourceRetrofitService {
    @GET("sources")
    fun getSourceDataFromApi(
        @Query("apiKey") apiKey : String
    ) : Call<Source>
}