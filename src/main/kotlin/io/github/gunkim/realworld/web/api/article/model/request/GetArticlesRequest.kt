package io.github.gunkim.realworld.web.api.article.model.request

data class GetArticlesRequest(
    val tag: String?,
    val author: String?,
    val favorited: String?,
    val limit: Int = 20,
    val offset: Int = 0,
)