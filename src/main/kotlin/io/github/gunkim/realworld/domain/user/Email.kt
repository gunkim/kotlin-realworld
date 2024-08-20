package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.ValueObject
import io.github.gunkim.realworld.domain.article.ArticleId
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Email(
    @Column(name = "email")
    val value: String,
) : ValueObject<Email>()
