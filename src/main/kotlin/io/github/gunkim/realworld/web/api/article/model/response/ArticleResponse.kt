package io.github.gunkim.realworld.web.api.article.model.response

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.web.api.user.model.response.ProfileResponse
import java.time.Instant

data class ArticleResponse(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val favorited: Boolean,
    val favoritesCount: Int,
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    val author: ProfileResponse,
) {
    companion object {
        fun from(
            article: Article,
            favoritesCount: Int,
            favorited: Boolean = false,
            authorFollowing: Boolean = false,
        ) = ArticleResponse(
            slug = article.slug.value,
            title = article.title,
            description = article.description,
            body = article.body,
            tagList = article.tags.map { it.name },
            createdAt = article.createdAt,
            updatedAt = article.updatedAt,
            favorited = favorited,
            favoritesCount = favoritesCount,
            author = ProfileResponse.of(
                article.author,
                authorFollowing
            )
        )

        fun noAuthenticated(article: Article, favoritesCount: Int) = from(
            article = article,
            favoritesCount = favoritesCount
        )

        fun create(article: Article) = from(
            article = article,
            favoritesCount = 0,
            favorited = false
        )
    }
}
