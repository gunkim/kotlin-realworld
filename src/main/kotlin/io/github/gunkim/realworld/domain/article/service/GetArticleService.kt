package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.ArticleReadRepository
import io.github.gunkim.realworld.domain.article.ArticleRepository
import org.springframework.stereotype.Service

@Service
class GetArticleService(
    private val articleRepository: ArticleRepository
) {
    fun getArticles(
        tag: String?,
        author: String?,
        limit: Int = 20,
        offset: Int = 0,
    ): List<Article> {
        return articleRepository.find(
            tag = tag,
            author = author,
            limit = limit,
            offset = offset
        )
    }

    fun getArticle(slug: String): Article {
        return articleRepository.findBySlug(slug)
    }
}