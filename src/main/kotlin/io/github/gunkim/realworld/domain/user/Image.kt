package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.net.URL

@Embeddable
data class Image(
    @Column(name = "image")
    val value: URL,
) : ValueObject<Image>()