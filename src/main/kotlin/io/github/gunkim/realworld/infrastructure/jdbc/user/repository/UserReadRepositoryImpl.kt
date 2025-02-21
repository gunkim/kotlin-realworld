package io.github.gunkim.realworld.infrastructure.jdbc.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository
import io.github.gunkim.realworld.infrastructure.jdbc.user.dao.FollowDao
import io.github.gunkim.realworld.infrastructure.jdbc.user.dao.UserDao
import org.springframework.stereotype.Repository

@Repository
class UserReadRepositoryImpl(
    private val userDao: UserDao,
    private val followDao: FollowDao,
) : UserReadRepository {
    override fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    override fun findById(userId: UserId): User? {
        return userDao.findById(userId)
    }

    override fun findByUserName(name: String): User? {
        return userDao.findByName(name)
    }

    override fun existsFollowerIdAndFolloweeId(followerId: UserId, followeeId: UserId): Boolean {
        // TODO: According to the original design intention, it is more appropriate to execute a SELECT query with the UUID condition using a native query rather than retrieving the user here.
        val id = userDao.findById(followerId)?.databaseId ?: throw IllegalArgumentException()
        val targetId = userDao.findById(followeeId)?.databaseId ?: throw IllegalArgumentException()

        return followDao.existsByFolloweeUserDatabaseIdAndFollowerUserDatabaseId(id, targetId)
    }

    override fun findFollowedUserIdsFor(userId: UserId): List<UserId> {
        return userDao.findAllByFollowerUserId(userId)
    }
}