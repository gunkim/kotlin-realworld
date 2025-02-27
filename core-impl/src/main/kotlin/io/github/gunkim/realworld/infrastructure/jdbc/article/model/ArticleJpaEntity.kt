package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.model.Tag
import io.github.gunkim.realworld.infrastructure.jdbc.share.Updatable
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import jakarta.persistence.AttributeConverter
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Converter(autoApply = true)
class SlugConverter : AttributeConverter<Slug, String> {
    override fun convertToDatabaseColumn(attribute: Slug): String = attribute.value
    override fun convertToEntityAttribute(slug: String): Slug = Slug.from(slug)
}

@Converter(autoApply = true)
class ArticleIdConverter : AttributeConverter<ArticleId, UUID> {
    override fun convertToDatabaseColumn(attribute: ArticleId): UUID = attribute.value
    override fun convertToEntityAttribute(uuid: UUID): ArticleId = ArticleId(uuid)
}

@Entity(name = "article")
// Soft Delete
@SQLDelete(sql = "UPDATE article SET deleted_at = now() WHERE article_id = ?")
@SQLRestriction("deleted_at is NULL")
class ArticleJpaEntity(
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    @Column(name = "uuid")
    @Convert(converter = ArticleIdConverter::class)
    override val id: ArticleId,
    slug: Slug,
    title: String,
    description: String,
    body: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId", nullable = false)
    override val author: io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity,
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val articleTagJpaEntities: List<ArticleTagJpaEntity> = listOf(),
    override val createdAt: Instant,
    override var updatedAt: Instant,
) : Article.Editor, Updatable {
    init {
        articleTagJpaEntities.forEach {
            it.article = this
        }
    }

    override val tags: List<Tag>
        get() = articleTagJpaEntities.map { Tag.create(it.tag.name) }

    @Convert(converter = SlugConverter::class)
    override var slug: Slug = slug
        set(value) {
            field = updateField(field, value)
        }
    override var title: String = title
        set(value) {
            field = updateField(field, value)
        }

    override var description: String = description
        set(value) {
            field = updateField(field, value)
        }

    override var body: String = body
        set(value) {
            field = updateField(field, value)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleJpaEntity

        if (databaseId != other.databaseId) return false
        if (id != other.id) return false
        if (author != other.author) return false
        if (articleTagJpaEntities != other.articleTagJpaEntities) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (slug != other.slug) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + id.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + articleTagJpaEntities.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + slug.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + body.hashCode()
        return result
    }

    override fun toString(): String {
        return "ArticleJpaEntity(databaseId=$databaseId, id=$id, author=$author, articleTagJpaEntities=$articleTagJpaEntities, createdAt=$createdAt, updatedAt=$updatedAt, slug=$slug, title='$title', description='$description', body='$body')"
    }

    companion object {
        fun from(article: Article): ArticleJpaEntity = with(article) {
            ArticleJpaEntity(
                databaseId = if (this is ArticleJpaEntity) databaseId else null,
                id = id,
                slug = slug,
                title = title,
                description = description,
                body = body,
                author = io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity.from(author),
                articleTagJpaEntities =
                    if (this is ArticleJpaEntity) articleTagJpaEntities
                    else article.tags.map(ArticleTagJpaEntity.Companion::from),
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }

        fun from(article: Article, tags: List<Tag>): ArticleJpaEntity = with(article) {
            ArticleJpaEntity(
                databaseId = if (this is ArticleJpaEntity) databaseId else null,
                id = id,
                slug = slug,
                title = title,
                description = description,
                body = body,
                articleTagJpaEntities = tags.map(ArticleTagJpaEntity.Companion::from),
                author = io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity.from(author),
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
            )
        }
    }
}
