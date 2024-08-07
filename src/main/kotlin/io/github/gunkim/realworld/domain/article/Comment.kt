package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.common.DomainEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Comment(
    @Id
    override val id: CommentId,
    body: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : DomainEntity<Comment, CommentId>() {
    var body = body
        protected set
    var updatedAt: LocalDateTime = createdAt
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    var article: Article? = null
        protected set

    @PrePersist
    fun prePersist() {
        this.updatedAt = LocalDateTime.now()
    }

    fun addArticle(article: Article) {
        this.article = article
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

