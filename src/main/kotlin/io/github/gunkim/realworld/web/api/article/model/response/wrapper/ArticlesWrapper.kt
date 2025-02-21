package io.github.gunkim.realworld.web.api.article.model.response.wrapper

import io.github.gunkim.realworld.web.api.article.model.response.ArticleResponse

data class ArticlesWrapper(
    val articles: List<ArticleResponse>,
    val articlesCount: Int,
) {
    companion object {
        fun create(
            articles: List<ArticleResponse>,
        ) = ArticlesWrapper(articles, articles.size)
    }
}