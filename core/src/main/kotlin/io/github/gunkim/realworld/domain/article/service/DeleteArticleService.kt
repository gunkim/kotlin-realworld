package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import org.springframework.stereotype.Service

@Service
class DeleteArticleService(
    override val articleRepository: ArticleRepository,
    private val articleOwnershipService: ArticleOwnershipService,
) : ArticleFindable {
    fun deleteArticle(
        slug: Slug,
        deleterId: UserId,
    ) {
        val article = findBySlug(slug)

        articleOwnershipService.validateOwnership(article, deleterId)

        articleRepository.delete(article)
    }
}