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
}
