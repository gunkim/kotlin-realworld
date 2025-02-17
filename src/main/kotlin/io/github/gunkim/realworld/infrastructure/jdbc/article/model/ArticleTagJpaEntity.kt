package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import io.github.gunkim.realworld.domain.article.Tag
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "article_tag")
data class ArticleTagJpaEntity(
    @Id
    @Column(name = "article_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id", nullable = false)
    var article: ArticleJpaEntity? = null,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [jakarta.persistence.CascadeType.PERSIST])
    @JoinColumn(name = "tag_id", nullable = false)
    val tag: TagJpaEntity,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
) : Tag {
    override val name: String
        get() = tag.name

    companion object {
        fun fromTagEntity(tag: TagJpaEntity) = ArticleTagJpaEntity(
            tag = tag,
        )

        fun from(tag: Tag) = ArticleTagJpaEntity(
            id = if (tag is ArticleTagJpaEntity) tag.id else null,
            article = if (tag is ArticleTagJpaEntity) tag.article else null,
            tag = if (tag is ArticleTagJpaEntity) tag.tag else TagJpaEntity.from(tag.name),
            createdAt = if (tag is ArticleTagJpaEntity) tag.createdAt else Instant.now(),
            updatedAt = if (tag is ArticleTagJpaEntity) tag.updatedAt else null,
        )
    }
}