package io.github.gunkim.realworld.infrastructure.jdbc.user.repository

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
        // TODO: According to the original design intention, it is more appropriate to execute a SELECT query with the UUID condition using a native query rather than retrieving the user here.
        val id = userDao.findByUuid(uuid)?.userId ?: throw IllegalArgumentException()
        val targetId = userDao.findByUuid(targetUuid)?.userId ?: throw IllegalArgumentException()

        followDao.save(FollowJpaEntity.of(id, targetId))
    }

    @Transactional
    override fun unfollow(uuid: UUID, targetUuid: UUID) {
        // TODO: According to the original design intention, it is more appropriate to execute a SELECT query with the UUID condition using a native query rather than retrieving the user here.
        val id = userDao.findByUuid(uuid)?.userId ?: throw IllegalArgumentException()
        val targetId = userDao.findByUuid(targetUuid)?.userId ?: throw IllegalArgumentException()

        followDao.deleteByFolloweeIdAndFollowerId(
            followingId = id,
            followerId = targetId
        )
    }
}