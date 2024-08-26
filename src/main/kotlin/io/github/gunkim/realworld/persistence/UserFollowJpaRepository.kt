package io.github.gunkim.realworld.persistence

import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserFollow
import io.github.gunkim.realworld.domain.user.UserFollowId
import io.github.gunkim.realworld.domain.user.UserFollowRepository
import org.springframework.data.jpa.repository.JpaRepository

interface UserFollowJpaRepository : JpaRepository<UserFollow, UserFollowId>, UserFollowRepository {
    override fun deleteByFollowerAndFollowee(follower: User, followee: User)
    override fun existsUserFollowByFollowerAndFollowee(follower: User, followee: User): Boolean
}