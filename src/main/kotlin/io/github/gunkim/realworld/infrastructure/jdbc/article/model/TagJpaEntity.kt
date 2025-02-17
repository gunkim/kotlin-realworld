package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import io.github.gunkim.realworld.domain.article.Tag
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "tag")
data class TagJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tagId: Int? = null,
    override val name: String,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
) : Tag {
    companion object {
        fun from(name: String) = TagJpaEntity(name = name, createdAt = Instant.now())
    }
}