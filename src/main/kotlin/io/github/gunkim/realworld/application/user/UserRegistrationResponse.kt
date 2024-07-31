package io.github.gunkim.realworld.application.user

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName
import io.github.gunkim.realworld.domain.user.User

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
data class UserRegistrationResponse(
    val username: String,
    val email: String,
    val password: String,
) {
    companion object {
        fun from(user: User) = UserRegistrationResponse(
            user.profile.name.value,
            user.email.value,
            user.password
        )
    }
}