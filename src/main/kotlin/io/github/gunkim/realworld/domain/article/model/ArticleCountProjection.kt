package io.github.gunkim.realworld.domain.article.model

interface ArticleCountProjection {
    val articleId: ArticleId
    val count: Int
}
