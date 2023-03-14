package com.example.newz.viewmodel

import android.icu.number.IntegerWidth
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newz.model.Article
import com.example.newz.model.Headline
import com.example.newz.model.News
import com.example.newz.model.NewsList
import com.example.newz.network.RetrofitInstance
import com.example.newz.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel : ViewModel() {

    private val apiKey = "87bb7a661afb4c2a8c962e6e0d8381a2"
    private var newsListData : MutableLiveData<List<Article>> = MutableLiveData()
    private var _totalCount : MutableLiveData<String> = MutableLiveData()
    val totalCount : LiveData<String>
    get() = _totalCount
    fun getNewsListDataObserver() : MutableLiveData<List<Article>>{
        return newsListData
    }

    fun makeApiCall(country : String , category : String , page : Int){
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(RetrofitService::class.java)
        val call : Call<Headline> = retrofitInstance.getDataFromApi(country,category,10,page,apiKey)
        call.enqueue(object : Callback<Headline>{
            override fun onResponse(call: Call<Headline>, response: Response<Headline>) {
                if (response.isSuccessful && response.body()?.articles != null){
                    newsListData.postValue(response.body()?.articles)
                    _totalCount.value = (response.body()?.totalResults)
                }
            }

            override fun onFailure(call: Call<Headline>, t: Throwable) {
                newsListData.postValue(null)
            }
        })
    }

}