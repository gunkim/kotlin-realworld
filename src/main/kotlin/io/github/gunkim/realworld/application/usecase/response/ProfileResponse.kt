package io.github.gunkim.realworld.application.usecase.response

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName
import io.github.gunkim.realworld.domain.user.User
import java.net.URL

@JsonTypeName("profile")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
data class ProfileResponse(
    val username: String,
    val bio: String?,
    val image: URL?,
    val following: Boolean,
) {
    companion object {
        fun of(user: User, following: Boolean) = ProfileResponse(
            username = user.profile.name.value,
            bio = user.profile.bio,
            image = user.profile.image?.value,
            following = following,
        )
    }
}