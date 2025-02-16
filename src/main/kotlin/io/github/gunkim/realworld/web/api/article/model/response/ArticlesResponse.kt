package io.github.gunkim.realworld.web.api.article.model.response

data class ArticlesResponse(
    val articles: List<ArticleResponse>,
    val articlesCount: Int,
) {
    companion object {
        fun create(
            articles: List<ArticleResponse>,
        ) = ArticlesResponse(articles, articles.size)
    }
}