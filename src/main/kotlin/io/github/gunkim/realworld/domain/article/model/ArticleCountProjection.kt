package io.github.gunkim.realworld.domain.article.model

interface ArticleCountProjection {
    fun getArticleId(): ArticleId
    fun getCount(): Int
}
