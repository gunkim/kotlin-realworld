package io.github.gunkim.realworld.api.article.model.request

data class FeedArticleRequest(
    val offset: Int = 0,
    val limit: Int = 20,
)