package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import org.springframework.stereotype.Service

@Service
class GetArticleService(
    override val articleRepository: ArticleRepository,
) : ArticleFindable {
    fun getArticles(
        tag: String?,
        author: String?,
        favoritedUsername: String?,
        limit: Int,
        offset: Int,
    ): List<Article> {
        return articleRepository.find(
            tag = tag,
            author = author,
            favoritedUsername = favoritedUsername,
            limit = limit,
            offset = offset
        )
    }

    fun feedArticles(
        userId: UserId,
        limit: Int,
        offset: Int,
    ): List<Article> {
        return articleRepository.findFeedArticles(
            userId = userId,
            limit = limit,
            offset = offset
        )
    }

    fun getArticle(slug: Slug): Article {
        return findBySlug(slug)
    }
}