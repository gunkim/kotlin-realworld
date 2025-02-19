package io.github.gunkim.realworld.share

data class PagingRequest(
    val offset: Int = 0,
    val limit: Int = 20,
)