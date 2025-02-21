package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.model.Comment
import io.github.gunkim.realworld.domain.user.model.UserId
import org.springframework.stereotype.Service

@Service
class CommentOwnershipService {
    fun validateOwnership(comment: Comment, authorId: UserId) {
        require(comment.author.id == authorId) {
            //TODO: A custom domain exception should be thrown.
            "The author is not the owner of this comment."
        }
    }
}