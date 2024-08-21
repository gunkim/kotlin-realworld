package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.common.DomainEntity
import jakarta.persistence.*
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class Tag(
    @Id
    override val id: TagId,
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime? = null,
) : DomainEntity<Tag, TagId>() {
    @ManyToOne(fetch = FetchType.LAZY)
    var article: Article? = null
        protected set

    @LastModifiedDate
    var updatedAt = updatedAt
        protected set

    fun addArticle(article: Article) {
        this.article = article
    }
}
