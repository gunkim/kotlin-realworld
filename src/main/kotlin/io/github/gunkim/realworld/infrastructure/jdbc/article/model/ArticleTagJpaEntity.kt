package io.github.gunkim.realworld.infrastructure.jdbc.article.model

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
class ArticleTagJpaEntity(
    @Id
    @Column(name = "article_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleTagJpaEntity

        if (databaseId != other.databaseId) return false
        if (article != other.article) return false
        if (tag != other.tag) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + (article?.hashCode() ?: 0)
        result = 31 * result + tag.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ArticleTagJpaEntity(databaseId=$databaseId, tag=$tag, createdAt=$createdAt, updatedAt=$updatedAt)"
    }


    companion object {
        fun fromTagEntity(tag: TagJpaEntity) = ArticleTagJpaEntity(
            tag = tag,
        )

        fun from(tag: Tag) = ArticleTagJpaEntity(
            databaseId = if (tag is ArticleTagJpaEntity) tag.databaseId else null,
            article = if (tag is ArticleTagJpaEntity) tag.article else null,
            tag = if (tag is ArticleTagJpaEntity) tag.tag else TagJpaEntity.from(tag.name),
            createdAt = if (tag is ArticleTagJpaEntity) tag.createdAt else Instant.now(),
            updatedAt = if (tag is ArticleTagJpaEntity) tag.updatedAt else null,
        )
    }
}