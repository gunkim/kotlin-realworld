package io.github.gunkim.realworld.web.api.article.model.request

data class UpdateArticleRequest(
    val title: String?,
    val description: String?,
    val body: String?,
)
