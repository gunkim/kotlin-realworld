package io.github.gunkim.realworld.web.api.comment.model.response.wrapper

import io.github.gunkim.realworld.web.api.comment.model.response.CommentResponse

data class CommentsWrapper(
    val comments: List<CommentResponse>,
)