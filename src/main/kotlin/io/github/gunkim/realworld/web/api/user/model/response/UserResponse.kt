package io.github.gunkim.realworld.web.api.user.model.response

import io.github.gunkim.realworld.domain.user.model.User

data class UserResponse(
    val email: String,
    val token: String,
    val username: String,
    val bio: String?,
    val image: String?,
) {
    companion object {
        fun from(user: User, token: String) = UserResponse(
            user.email,
            token,
            user.name,
            user.bio,
            user.image
        )
    }
}
