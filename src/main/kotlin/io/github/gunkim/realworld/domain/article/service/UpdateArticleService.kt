package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.ArticleRepository
import io.github.gunkim.realworld.domain.article.Slug
import org.springframework.stereotype.Service

private fun Article.Editor.setTitleAndSlug(
    title: String?,
) {
    if (title.isNullOrBlank()) return
    this.title = title
    this.slug = Slug.fromTitle(title)
}

@Service
class UpdateArticleService(
    override val articleRepository: ArticleRepository,
) : ArticleFindable {
    fun updateArticle(
        slug: Slug,
        title: String?,
        description: String?,
        body: String?,
    ): Article {
        val article = super.findBySlug(slug)

        return articleRepository.save(article.edit().apply {
            setTitleAndSlug(title)
            this.description = description ?: article.description
            this.body = body ?: article.body
        })
    }
}