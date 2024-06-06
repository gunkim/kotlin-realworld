package io.github.gunkim.realworld.domain.entity

import io.github.gunkim.realworld.domain.vo.ArticleId
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Article(
    @Id
    val id: ArticleId,
    title: String,
    description: String,
    body: String,
    val createdAt: LocalDateTime,
    tags: MutableSet<Tag> = mutableSetOf(),
    comments: MutableSet<Comment> = mutableSetOf(),
) {
    var title = title
        protected set
    var description = description
        protected set
    var body = body
        protected set
    var updatedAt: LocalDateTime? = null
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

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}
