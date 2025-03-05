package io.github.gunkim.realworld.infrastructure.jpa.article.model

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.model.Tag
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.infrastructure.jpa.share.Updatable
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserEntity
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
class ArticleEntity(
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
    @Convert(converter = ArticleIdConverter::class)
    override val id: ArticleId,
    slug: Slug,
    title: String,
    description: String,
    body: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    val authorEntity: UserEntity,
    @OneToMany(mappedBy = "articleEntity", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val tagEntities: List<ArticleTagEntity> = listOf(),
    override val createdAt: Instant,
    override var updatedAt: Instant,
) : Article.Editor, Updatable {
    init {
        tagEntities.forEach {
            it.articleEntity = this
        }
    }

    @OneToMany(mappedBy = "articleEntity", fetch = FetchType.LAZY)
    var favoriteEntities: List<FavoriteEntity>? = listOf()

    override val author: User
        get() = authorEntity

    override val tags: List<Tag>
        get() = tagEntities
            .map { Tag.create(it.tagEntity.name) }
            .sortedBy { it.name }

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

        other as ArticleEntity

        if (databaseId != other.databaseId) return false
        if (id != other.id) return false
        if (authorEntity != other.authorEntity) return false
        if (tagEntities != other.tagEntities) return false
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
        result = 31 * result + authorEntity.hashCode()
        result = 31 * result + tagEntities.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + slug.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + body.hashCode()
        return result
    }

    companion object {
        fun from(article: Article): ArticleEntity = with(article) {
            ArticleEntity(
                databaseId = if (this is ArticleEntity) databaseId else null,
                id = id,
                slug = slug,
                title = title,
                description = description,
                body = body,
                authorEntity = UserEntity.from(author),
                tagEntities =
                    if (this is ArticleEntity) tagEntities
                    else article.tags.map(ArticleTagEntity.Companion::from),
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }

        fun from(article: Article, tags: List<Tag>): ArticleEntity = with(article) {
            ArticleEntity(
                databaseId = if (this is ArticleEntity) databaseId else null,
                id = id,
                slug = slug,
                title = title,
                description = description,
                body = body,
                tagEntities = tags.map(ArticleTagEntity.Companion::from),
                authorEntity = UserEntity.from(author),
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
            )
        }
    }
}
