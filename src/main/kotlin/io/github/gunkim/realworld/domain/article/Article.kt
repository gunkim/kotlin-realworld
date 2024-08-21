package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.common.AggregateRoot
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class Article(
    @Id
    override val id: ArticleId,
    title: String,
    description: String,
    body: String,
    tags: MutableSet<Tag> = mutableSetOf(),
    comments: MutableSet<Comment> = mutableSetOf(),
    @CreatedDate
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime? = null,
) : AggregateRoot<Article, ArticleId>() {
    var title = title
        protected set
    var description = description
        protected set
    var body = body
        protected set

    @LastModifiedDate
    var updatedAt = updatedAt
        protected set

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private val _tags = tags.toMutableSet()
    val tags: Set<Tag> get() = _tags.toSet()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private val _comments = comments.toMutableSet()
    val comments: Set<Comment> get() = _comments.toSet()

    fun addTag(tag: Tag) {
        _tags.add(tag)
        tag.addArticle(this)
    }

    fun addComment(comment: Comment) {
        _comments.add(comment)
        comment.addArticle(this)
    }
}
