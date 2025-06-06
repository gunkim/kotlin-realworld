package io.github.gunkim.realworld.infrastructure.jpa.article.model

import io.github.gunkim.realworld.domain.article.model.Tag
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
class ArticleTagEntity(
    @Id
    @Column(name = "article_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id", nullable = false)
    var articleEntity: ArticleEntity? = null,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [jakarta.persistence.CascadeType.PERSIST])
    @JoinColumn(name = "tag_id", nullable = false)
    val tagEntity: TagEntity,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
) : Tag {
    override val name: String
        get() = tagEntity.name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleTagEntity

        if (databaseId != other.databaseId) return false
        if (articleEntity != other.articleEntity) return false
        if (tagEntity != other.tagEntity) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + (articleEntity?.hashCode() ?: 0)
        result = 31 * result + tagEntity.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromTagEntity(tag: TagEntity) = ArticleTagEntity(
            tagEntity = tag,
        )

        fun from(tag: Tag) = ArticleTagEntity(
            databaseId = if (tag is ArticleTagEntity) tag.databaseId else null,
            articleEntity = if (tag is ArticleTagEntity) tag.articleEntity else null,
            tagEntity = if (tag is ArticleTagEntity) tag.tagEntity else TagEntity.from(tag.name),
            createdAt = if (tag is ArticleTagEntity) tag.createdAt else Instant.now(),
            updatedAt = if (tag is ArticleTagEntity) tag.updatedAt else null,
        )
    }
}