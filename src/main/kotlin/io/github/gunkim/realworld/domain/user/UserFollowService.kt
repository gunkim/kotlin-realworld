package io.github.gunkim.realworld.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserFollowService(
    private val userFollowRepository: UserFollowRepository,
) {
    fun follow(follower: User, followee: User) {
        UserFollow.of(follower, followee).let {
            userFollowRepository.save(it)
        }
    }

    fun unfollow(follower: User, followee: User) {
        userFollowRepository.deleteByFollowerAndFollowee(follower, followee)
    }
}