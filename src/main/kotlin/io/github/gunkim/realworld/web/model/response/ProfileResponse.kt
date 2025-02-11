package io.github.gunkim.realworld.web.model.response

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName
import io.github.gunkim.realworld.domain.user.model.User
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
            username = user.name,
            bio = user.bio,
            image = user.image,
            following = following,
        )
    }
}