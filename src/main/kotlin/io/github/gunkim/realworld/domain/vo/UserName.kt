package io.github.gunkim.realworld.domain.vo

import io.github.gunkim.realworld.domain.base.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserName(
    @Column(name = "name")
    val value: String,
) : ValueObject<ArticleId>()
