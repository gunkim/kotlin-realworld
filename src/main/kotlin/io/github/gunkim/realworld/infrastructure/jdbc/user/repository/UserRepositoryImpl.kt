package io.github.gunkim.realworld.infrastructure.jdbc.user.repository

import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
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

    override fun follow(uuid: UUID, targetUuid: UUID) {
        val followerId = getUserIdOrThrow(uuid)
        val followeeId = getUserIdOrThrow(targetUuid)

        followDao.save(FollowJpaEntity.of(followeeId, followerId))
    }

    override fun unfollow(uuid: UUID, targetUuid: UUID) {
        val followerId = getUserIdOrThrow(uuid)
        val followeeId = getUserIdOrThrow(targetUuid)

        followDao.deleteByFolloweeIdAndFollowerId(
            followingId = followeeId,
            followerId = followerId
        )
    }

    private fun getUserIdOrThrow(uuid: UUID): Int {
        return userDao.findByUuid(uuid)?.userId
            ?: throw UserNotFoundException.fromUUID(uuid)
    }
}