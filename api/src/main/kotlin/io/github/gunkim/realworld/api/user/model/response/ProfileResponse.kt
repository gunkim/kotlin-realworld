package io.github.gunkim.realworld.api.user.model.response

import io.github.gunkim.realworld.domain.user.model.User

data class ProfileResponse(
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean,
) {
    companion object {
        fun of(user: User, following: Boolean) = ProfileResponse(
            username = user.name,
            bio = user.bio,
            image = user.image,
            following = following,
        )
    }
}