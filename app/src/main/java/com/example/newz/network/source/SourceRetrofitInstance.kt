package com.example.newz.network.source

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SourceRetrofitInstance {
    companion object{
        private const val BASE_URL = "https://newsapi.org/v2/top-headlines/"
        fun getSourceRetrofitInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}