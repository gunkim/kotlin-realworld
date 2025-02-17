package io.github.gunkim.realworld.web.api.article.model.request

data class CreateArticleRequest(
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String> = listOf(),
)