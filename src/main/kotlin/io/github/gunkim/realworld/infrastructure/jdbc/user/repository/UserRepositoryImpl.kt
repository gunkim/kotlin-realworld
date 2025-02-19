package io.github.gunkim.realworld.infrastructure.jdbc.user.repository

import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import io.github.gunkim.realworld.infrastructure.jdbc.user.dao.FollowDao
import io.github.gunkim.realworld.infrastructure.jdbc.user.dao.UserDao
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.FollowJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import java.util.UUID
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
        val followerId = getUserDatabaseIdOrThrow(followerId)
        val followeeId = getUserDatabaseIdOrThrow(followeeId)

        followDao.save(FollowJpaEntity.of(followeeId, followerId))
    }

    override fun unfollow(followerId: UserId, followeeId: UserId) {
        val followerId = getUserDatabaseIdOrThrow(followerId)
        val followeeId = getUserDatabaseIdOrThrow(followeeId)

        followDao.deleteByFolloweeIdAndFollowerId(
            followingId = followeeId,
            followerId = followerId
        )
    }

    private fun getUserDatabaseIdOrThrow(userId: UserId): Int {
        return userDao.findByUuid(userId.value)?.userId
            ?: throw UserNotFoundException.fromId(userId)
    }
}