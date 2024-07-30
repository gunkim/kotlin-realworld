package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.common.DomainEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Tag(
    @Id
    override val id: TagId,
    val name: String,
) : DomainEntity<Tag, TagId>() {
    @ManyToOne(fetch = FetchType.LAZY)
    var article: Article? = null
        protected set

    fun addArticle(article: Article) {
        this.article = article
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
