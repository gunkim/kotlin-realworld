package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.common.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
class TagId(
    @Column(name = "id")
    val value: UUID = UUID.randomUUID(),
) : ValueObject<ArticleId>()
