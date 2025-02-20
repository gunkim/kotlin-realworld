package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.UserFindable
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.model.Tag
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserRepository
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
        authorId: UserId,
    ): Article {
        val author = super.getUserById(authorId)

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