package io.github.gunkim.realworld.domain.vo

import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
data class CommentId(
    val value: UUID,
)
