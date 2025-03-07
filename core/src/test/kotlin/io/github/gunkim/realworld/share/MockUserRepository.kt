package io.github.gunkim.realworld.share

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserRepository

class MockUserRepository : UserRepository {
    override fun save(user: User): User {
        val existingUser = InMemoryDatabase.users[user.id]
        InMemoryDatabase.users[user.id] = user

        if (existingUser != null) {
            InMemoryDatabase.articles.replaceAll { _, article ->
                if (article.author.id == user.id) {
                    if (article is Article.Companion.Model) {
                        article.copy(author = user)
                    } else {
                        article
                    }
                } else {
                    article
                }
            }
        }
        return user
    }

    override fun follow(followerId: UserId, followeeId: UserId) {
        val followingSet = InMemoryDatabase.followings.getOrPut(followerId) { mutableSetOf() }
        followingSet.add(followeeId)
    }

    override fun unfollow(followerId: UserId, followeeId: UserId) {
        InMemoryDatabase.followings[followerId]?.remove(followeeId)
    }

    override fun findByEmail(email: String): User? {
        return InMemoryDatabase.users.values.find { it.email == email }
    }

    override fun findById(userId: UserId): User? {
        return InMemoryDatabase.users[userId]
    }

    override fun findByUserName(name: String): User? {
        return InMemoryDatabase.users.values.find { it.name == name }
    }

    override fun findFollowedUserIdsFor(userId: UserId): List<UserId> {
        return InMemoryDatabase.followings[userId]?.toList() ?: emptyList()
    }
}