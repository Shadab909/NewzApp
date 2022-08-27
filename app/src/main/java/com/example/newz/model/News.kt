package com.example.newz.model

data class News (
    val title : String ,
    val author : String,
    val url : String ,
    val imageUrl : String
    )

data class NewsList(val items : ArrayList<News>)

