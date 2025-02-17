package io.github.gunkim.realworld.domain.article.model

import java.util.UUID

interface ArticleCountProjection {
    fun getUuid(): UUID
    fun getCount(): Int
}
