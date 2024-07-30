package io.github.gunkim.realworld.domain.vo

import io.github.gunkim.realworld.domain.base.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
data class CommentId(
    @Column(name = "id")
    val value: UUID,
) : ValueObject<ArticleId>()
