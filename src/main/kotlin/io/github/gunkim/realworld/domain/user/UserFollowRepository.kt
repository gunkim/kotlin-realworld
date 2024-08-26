package io.github.gunkim.realworld.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserFollowRepository : JpaRepository<UserFollow, UserFollowId> {
    fun deleteByFollowerAndFollowee(follower: User, followee: User)
    fun existsUserFollowByFollowerAndFollowee(follower: User, followee: User): Boolean
}