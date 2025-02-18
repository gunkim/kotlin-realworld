package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.service.ArticleOwnershipService
import io.github.gunkim.realworld.domain.article.service.DeleteArticleService
import io.github.gunkim.realworld.domain.article.service.GetArticleService
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class DeleteArticleUseCase(
    private val getArticleService: GetArticleService,
    private val articleOwnershipService: ArticleOwnershipService,
    private val deleteArticleService: DeleteArticleService,
) {
    fun deleteArticle(slug: String, authUuid: UUID) {
        val article = getArticleService.getArticle(Slug(slug))

        articleOwnershipService.validateOwnership(article, authUuid)

        deleteArticleService.deleteArticle(article)
    }
}