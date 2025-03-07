package io.github.gunkim.realworld.share

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId

object InMemoryDatabase {
    val users: MutableMap<UserId, User> = mutableMapOf() // User 저장소
    val articles: MutableMap<ArticleId, Article> = mutableMapOf() // Article 저장소

    val followings: MutableMap<UserId, MutableSet<UserId>> = mutableMapOf()
    val favorites: MutableMap<ArticleId, MutableSet<UserId>> = mutableMapOf()

    // 테스트 실행 전후 데이터를 초기화하기 위한 메서드
    fun clear() {
        users.clear()
        articles.clear()
        followings.clear()
        favorites.clear()
    }
}