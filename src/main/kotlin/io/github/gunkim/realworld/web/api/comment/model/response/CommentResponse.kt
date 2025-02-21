package io.github.gunkim.realworld.web.api.comment.model.response

import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.web.api.user.model.response.ProfileResponse
import java.time.Instant

data class CommentResponse(
    val id: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val body: String,
    val author: ProfileResponse,
) {
    companion object {
        fun from(comment: Comment, isFollowing: Boolean) = CommentResponse(
            comment.id.value,
            comment.createdAt,
            comment.updatedAt,
            comment.body,
            ProfileResponse.of(comment.author, isFollowing)
        )
    }
}