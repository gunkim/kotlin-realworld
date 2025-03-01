package io.github.gunkim.realworld.infrastructure.jpa.user.repository

import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import io.github.gunkim.realworld.infrastructure.jpa.user.dao.FollowDao
import io.github.gunkim.realworld.infrastructure.jpa.user.dao.UserDao
import io.github.gunkim.realworld.infrastructure.jpa.user.model.FollowEntity
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserEntity
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
        return userDao.save(UserEntity.from(user))
    }

    override fun follow(followerId: UserId, followeeId: UserId) {
        val (follower, followee) = resolveUserEntity(followerId, followeeId)
        followDao.save(
            FollowEntity.of(
                followerUserEntity = follower,
                followeeUserEntity = followee
            )
        )
    }

    override fun unfollow(followerId: UserId, followeeId: UserId) {
        val (follower, followee) = resolveUserEntity(followerId, followeeId)
        followDao.deleteByFolloweeEntityAndFollowerEntity(
            followeeUserEntity = followee,
            followerUserEntity = follower
        )
    }

    private fun resolveUserEntity(followerId: UserId, followeeId: UserId): Pair<UserEntity, UserEntity> {
        val follower = getUserEntityOrThrow(followerId)
        val followee = getUserEntityOrThrow(followeeId)

        return follower to followee
    }

    private fun getUserEntityOrThrow(userId: UserId): UserEntity {
        return userDao.findById(userId)
            ?: throw UserNotFoundException.fromId(userId)
    }
}