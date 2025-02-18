package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import org.springframework.stereotype.Service

@Service
class DeleteArticleService(
    private val articleRepository: ArticleRepository,
) {
    fun deleteArticle(
        article: Article,
    ) {
        articleRepository.delete(article)
    }
}