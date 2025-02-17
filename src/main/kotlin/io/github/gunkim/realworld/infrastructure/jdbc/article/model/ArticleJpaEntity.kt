package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.Tag
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import jakarta.persistence.CascadeType
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

//TODO : Refactoring
@Entity(name = "article")
class ArticleJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val articleId: Int? = null,
    override val uuid: UUID,
    slug: String,
    title: String,
    description: String,
    body: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId", nullable = false)
    override val author: UserJpaEntity,
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val articleTagJpaEntities: List<ArticleTagJpaEntity> = listOf(),
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    override val comments: List<CommentJpaEntity> = listOf(),
    override val createdAt: Instant,
    override var updatedAt: Instant = Instant.now(),
) : Article.Editor {
    init {
        articleTagJpaEntities.forEach {
            it.article = this
        }
    }

    override val tags: List<Tag>
        get() = articleTagJpaEntities.map { Tag.create(it.tag.name) }

    override var slug: String = slug
        set(value) {
            field = value
            updatedAt = Instant.now()
        }
    override var title: String = title
        set(value) {
            field = value
            updatedAt = Instant.now()
        }

    override var description: String = description
        set(value) {
            field = value
            updatedAt = Instant.now()
        }

    override var body: String = body
        set(value) {
            field = value
            updatedAt = Instant.now()
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleJpaEntity

        if (articleId != other.articleId) return false
        if (uuid != other.uuid) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (body != other.body) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (author != other.author) return false
        if (tags != other.tags) return false
        if (comments != other.comments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = articleId.hashCode()
        result = 31 * result + uuid.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + comments.hashCode()
        return result
    }

    companion object {
        fun from(article: Article, tags: List<TagJpaEntity>): ArticleJpaEntity = with(article) {
            ArticleJpaEntity(
                articleId = if (this is ArticleJpaEntity) articleId else null,
                uuid = uuid,
                slug = slug,
                title = title,
                description = description,
                body = body,
                articleTagJpaEntities = tags.map { ArticleTagJpaEntity(tag = it) },
                author = UserJpaEntity.from(author),
                createdAt = Instant.now()
            )
        }
    }
}
