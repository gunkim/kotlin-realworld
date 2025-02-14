package io.github.gunkim.realworld.infrastructure.jdbc.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository
import io.github.gunkim.realworld.infrastructure.jdbc.user.dao.FollowDao
import io.github.gunkim.realworld.infrastructure.jdbc.user.dao.UserDao
import java.util.UUID
import org.springframework.stereotype.Repository

@Repository
class UserReadRepositoryImpl(
    private val userDao: UserDao,
    private val followDao: FollowDao,
) : UserReadRepository {
    override fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    override fun findByUuid(uuid: UUID): User? {
        return userDao.findByUuid(uuid)
    }

    override fun findByUserName(name: String): User? {
        return userDao.findByName(name)
    }

    override fun existsFollowingIdAndFollowerUsername(uuid: UUID, targetUuid: UUID): Boolean {
        // TODO: According to the original design intention, it is more appropriate to execute a SELECT query with the UUID condition using a native query rather than retrieving the user here.
        val id = userDao.findByUuid(uuid)?.userId ?: throw IllegalArgumentException()
        val targetId = userDao.findByUuid(targetUuid)?.userId ?: throw IllegalArgumentException()

        return followDao.existsByFolloweeIdAndFollowerId(id, targetId)
    }

    override fun findFollowingUserUuids(uuid: UUID): List<UUID> {
        return userDao.findAllByFollowerUserUuid(uuid)
    }
}