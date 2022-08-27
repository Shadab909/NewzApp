package com.example.newz.model

data class Headline(
    val articles: List<Article>,
    val status: String,
    val totalResults: String
)