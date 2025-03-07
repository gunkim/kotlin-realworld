package io.github.gunkim.realworld.share

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId

data class ArticleCountProjectionImpl(
    override val articleId: ArticleId,
    override val count: Int,
) : ArticleCountProjection

class MockArticleRepository : ArticleRepository {
    override fun save(article: Article): Article {
        val updatedAuthor: User = InMemoryDatabase.users[article.author.id] ?: article.author
        val updatedArticle = if (article is Article.Companion.Model) {
            article.copy(author = updatedAuthor)
        } else {
            article
        }
        InMemoryDatabase.articles[updatedArticle.id] = updatedArticle
        return updatedArticle
    }

    override fun delete(article: Article) {
        InMemoryDatabase.articles.remove(article.id)
        InMemoryDatabase.favorites.remove(article.id)
    }

    override fun favorite(article: Article, user: User) {
        val favoritesSet = InMemoryDatabase.favorites.getOrPut(article.id) { mutableSetOf() }
        favoritesSet.add(user.id)
    }

    override fun unFavorite(article: Article, user: User) {
        InMemoryDatabase.favorites[article.id]?.remove(user.id)
    }

    override fun find(
        tag: String?,
        author: String?,
        favoritedUsername: String?,
        limit: Int,
        offset: Int,
    ): List<Article> {
        var filtered = InMemoryDatabase.articles.values.toList().map { it }

        if (tag != null) {
            filtered = filtered.filter { article ->
                article.tags.any { it.name == tag }
            }
        }

        if (author != null) {
            filtered = filtered.filter { article ->
                article.author.name == author
            }
        }

        if (favoritedUsername != null) {
            val favoriter = InMemoryDatabase.users.values
                .map { it }
                .find { it.name == favoritedUsername }
            filtered = if (favoriter != null) {
                filtered.filter { article ->
                    InMemoryDatabase.favorites[article.id]?.contains(favoriter.id) ?: false
                }
            } else {
                emptyList()
            }
        }

        return filtered.drop(offset).take(limit)
    }

    override fun findFeedArticles(userId: UserId, limit: Int, offset: Int): List<Article> {
        val followedUserIds = InMemoryDatabase.followings[userId] ?: emptySet()
        val feedArticles = InMemoryDatabase.articles.values.toList().map { it }
            .filter { article ->
                followedUserIds.contains(article.author.id)
            }
        return feedArticles.drop(offset).take(limit)
    }

    override fun getCountAllByArticleIds(articleIds: List<ArticleId>): List<ArticleCountProjection> {
        return articleIds.map { articleId ->
            val count = InMemoryDatabase.favorites[articleId]?.size ?: 0
            ArticleCountProjectionImpl(articleId, count)
        }
    }

    override fun getFavoritesArticleIds(userId: UserId): List<ArticleId> {
        return InMemoryDatabase.favorites.filter { (_, userIds) ->
            userIds.contains(userId)
        }.keys.toList()
    }

    override fun findBySlug(slug: Slug): Article? {
        return InMemoryDatabase.articles.values
            .map { it }
            .find { it.slug == slug }
    }
}