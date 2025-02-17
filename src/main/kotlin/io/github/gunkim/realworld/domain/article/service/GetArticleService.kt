package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.ArticleRepository
import io.github.gunkim.realworld.domain.article.Slug
import org.springframework.stereotype.Service

@Service
class GetArticleService(
    override val articleRepository: ArticleRepository,
) : ArticleFindable {
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

    fun getArticle(slug: Slug): Article {
        return findBySlug(slug)
    }
}