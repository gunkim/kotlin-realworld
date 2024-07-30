package io.github.gunkim.realworld.domain.entity

import io.github.gunkim.realworld.domain.vo.TagId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Tag(
    @Id
    val id: TagId,
    val name: String,
) {
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
