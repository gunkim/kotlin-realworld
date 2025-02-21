package io.github.gunkim.realworld.web.api.comment.model.response

import com.fasterxml.jackson.annotation.JsonTypeInfo

data class CommentsResponse(
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    val comments: List<CommentResponse>,
)