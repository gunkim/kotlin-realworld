package io.github.gunkim.realworld.domain.article

import java.util.UUID

interface ArticleCountProjection {
    fun getUuid(): UUID
    fun getCount(): Int
}
