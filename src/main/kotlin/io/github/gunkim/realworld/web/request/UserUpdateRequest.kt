package io.github.gunkim.realworld.web.request

import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.Image
import io.github.gunkim.realworld.domain.user.UserName
import java.net.URI

class UserUpdateRequest(
    email: String?,
    username: String?,
    image: String?,
    val password: String?,
    val bio: String?,
) {
    val email = email?.let { Email(it) }
    val username = username?.let { UserName(it) }
    val image = image
        ?.let(URI::create)
        ?.let(URI::toURL)
        ?.let(::Image)
}