package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.Comment
import org.springframework.stereotype.Service

@Service
class CommentValidationService {
    fun validateCommentBelongsToArticle(comment: Comment, article: Article) {
        require(comment.article == article) {
            //TODO: A custom domain exception should be thrown.
            "Comment does not belong to the specified article.(comment.article: ${comment.article}, article: $article)"
        }
    }
}
