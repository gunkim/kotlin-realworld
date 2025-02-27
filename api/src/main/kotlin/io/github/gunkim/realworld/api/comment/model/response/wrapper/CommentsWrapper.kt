package io.github.gunkim.realworld.api.comment.model.response.wrapper

import io.github.gunkim.realworld.api.comment.model.response.CommentResponse

data class CommentsWrapper(
    val comments: List<CommentResponse>,
)