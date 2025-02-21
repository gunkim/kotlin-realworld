package io.github.gunkim.realworld.web.api.comment.model.response

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName
import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.web.api.user.model.response.ProfileResponse
import java.time.Instant

@JsonTypeName("comment")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
data class CommentResponse(
    val id: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val body: String,
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
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