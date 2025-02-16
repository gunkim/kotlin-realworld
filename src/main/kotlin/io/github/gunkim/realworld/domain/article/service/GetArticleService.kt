package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.ArticleReadRepository
import org.springframework.stereotype.Service

@Service
class GetArticleService(
    private val articleReadRepository: ArticleReadRepository,
) {
    fun getArticles(
        tag: String?,
        author: String?,
        limit: Int = 20,
        offset: Int = 0,
    ): List<Article> {
        return articleReadRepository.find(
            tag = tag,
            author = author,
            limit = limit,
            offset = offset
        )
    }

    fun getArticle(slug: String): Article {
        return articleReadRepository.findBySlug(slug)
    }
}