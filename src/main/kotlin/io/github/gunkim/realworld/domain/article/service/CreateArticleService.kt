package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.UserFindable
import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.ArticleRepository
import io.github.gunkim.realworld.domain.article.Slug
import io.github.gunkim.realworld.domain.article.Tag
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class CreateArticleService(
    private val articleRepository: ArticleRepository,
    override val userRepository: UserRepository,
) : UserFindable {
    fun createArticle(
        title: String,
        description: String,
        body: String,
        tagList: List<String>,
        authorUuid: UUID,
    ): Article {
        val author = super.getUserByUUID(authorUuid)

        return articleRepository.save(
            Article.create(
                slug = Slug.fromTitle(title),
                title = title,
                description = description,
                body = body,
                tags = tagList.map { Tag.create(it) },
                author = author
            )
        )
    }
}