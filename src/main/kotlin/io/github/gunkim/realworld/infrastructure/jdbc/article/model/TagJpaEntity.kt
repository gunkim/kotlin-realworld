package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "tag")
data class TagJpaEntity(
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun from(name: String): TagJpaEntity {
            val now = Instant.now()

            return TagJpaEntity(
                name = name,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}