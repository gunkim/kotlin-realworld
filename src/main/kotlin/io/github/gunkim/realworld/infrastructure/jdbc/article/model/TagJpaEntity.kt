package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import io.github.gunkim.realworld.domain.article.Tag
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.Instant

//TODO : Refactoring
@Entity(name = "tag")
class TagJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tagId: Int? = null,
    override val name: String,
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = createdAt,
) : Tag {
    @ManyToOne
    val article: ArticleJpaEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TagJpaEntity

        if (tagId != other.tagId) return false
        if (name != other.name) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }
}