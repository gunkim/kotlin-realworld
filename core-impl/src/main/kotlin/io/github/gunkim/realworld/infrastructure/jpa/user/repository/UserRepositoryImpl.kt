package io.github.gunkim.realworld.infrastructure.jpa.user.repository

import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import io.github.gunkim.realworld.infrastructure.jpa.user.dao.FollowDao
import io.github.gunkim.realworld.infrastructure.jpa.user.dao.UserDao
import io.github.gunkim.realworld.infrastructure.jpa.user.model.FollowJpaEntity
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserJpaEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class UserRepositoryImpl(
    private val userDao: UserDao,
    private val followDao: FollowDao,
    @Qualifier("userReadRepositoryImpl")
    private val userReadRepository: UserReadRepository,
) : UserRepository, UserReadRepository by userReadRepository {
    override fun save(user: User): User {
        return userDao.save(UserJpaEntity.from(user))
    }

    override fun follow(followerId: UserId, followeeId: UserId) {
        val (followerDatabaseId, followeeDatabaseId) = resolveUserDatabaseIds(followerId, followeeId)
        followDao.save(
            FollowJpaEntity.of(
                followeeId = followeeDatabaseId,
                followerId = followerDatabaseId
            )
        )
    }

    override fun unfollow(followerId: UserId, followeeId: UserId) {
        val (followerDatabaseId, followeeDatabaseId) = resolveUserDatabaseIds(followerId, followeeId)
        followDao.deleteByFolloweeUserDatabaseIdAndFollowerUserDatabaseId(
            followingDatabaseId = followeeDatabaseId,
            followerDatabaseId = followerDatabaseId
        )
    }

    private fun resolveUserDatabaseIds(followerId: UserId, followeeId: UserId): Pair<Int, Int> {
        val followerDatabaseId = getUserDatabaseIdOrThrow(followerId)
        val followeeDatabaseId = getUserDatabaseIdOrThrow(followeeId)

        return followerDatabaseId to followeeDatabaseId
    }

    private fun getUserDatabaseIdOrThrow(userId: UserId): Int {
        return userDao.findById(userId)?.databaseId
            ?: throw UserNotFoundException.fromId(userId)
    }
}