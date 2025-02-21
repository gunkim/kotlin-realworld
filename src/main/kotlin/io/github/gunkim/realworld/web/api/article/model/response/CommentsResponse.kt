package io.github.gunkim.realworld.web.api.article.model.response

import com.fasterxml.jackson.annotation.JsonTypeInfo

data class CommentsResponse(
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    val comments: List<CommentResponse>,
)