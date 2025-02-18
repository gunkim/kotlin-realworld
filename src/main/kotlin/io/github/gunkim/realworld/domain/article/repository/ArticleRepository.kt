package io.github.gunkim.realworld.domain.article.repository

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.user.model.User

interface ArticleRepository : ArticleReadRepository {
    fun save(article: Article): Article
    fun delete(article: Article)

    fun favorite(article: Article, user: User)
}