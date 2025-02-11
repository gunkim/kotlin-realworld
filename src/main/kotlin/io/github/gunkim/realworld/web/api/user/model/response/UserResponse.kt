package io.github.gunkim.realworld.web.api.user.model.response

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName
import io.github.gunkim.realworld.domain.user.model.User

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
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
