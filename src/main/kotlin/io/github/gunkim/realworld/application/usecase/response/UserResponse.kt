package io.github.gunkim.realworld.application.usecase.response

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName
import io.github.gunkim.realworld.domain.user.User
import java.net.URL

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
data class UserResponse(
    val email: String,
    val token: String,
    val username: String,
    val bio: String?,
    val image: URL?,
) {
    companion object {
        fun from(user: User, token: String) = UserResponse(
            user.email.value,
            token,
            user.profile.name.value,
            user.profile.bio,
            user.profile.image?.value
        )
    }
}
