package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.common.DomainEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class Comment(
    @Id
    override val id: CommentId,
    body: String,
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime? = null,
) : DomainEntity<Comment, CommentId>() {
    var body = body
        protected set

    @LastModifiedDate
    var updatedAt = updatedAt
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    var article: Article? = null
        protected set

    fun addArticle(article: Article) {
        this.article = article
    }
}

