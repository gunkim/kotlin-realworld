package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.common.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
data class CommentId(
    @Column(name = "id")
    val value: UUID,
) : ValueObject<CommentId>()
