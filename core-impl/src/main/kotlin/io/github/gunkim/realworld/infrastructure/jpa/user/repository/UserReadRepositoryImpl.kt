package io.github.gunkim.realworld.infrastructure.jpa.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository
import io.github.gunkim.realworld.infrastructure.jpa.user.dao.UserDao
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class UserReadRepositoryImpl(
    private val userDao: UserDao,
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

    override fun findFollowedUserIdsFor(userId: UserId): List<UserId> {
        return userDao.findAllByFollowerUserId(userId)
    }
}