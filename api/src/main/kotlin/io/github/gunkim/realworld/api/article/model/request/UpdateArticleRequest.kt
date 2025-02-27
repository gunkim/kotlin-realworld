package io.github.gunkim.realworld.api.article.model.request

data class UpdateArticleRequest(
    val title: String?,
    val description: String?,
    val body: String?,
)
