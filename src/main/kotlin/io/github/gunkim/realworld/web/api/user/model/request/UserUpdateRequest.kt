package io.github.gunkim.realworld.web.api.user.model.request

import java.net.URL

class UserUpdateRequest(
    val email: String?,
    val username: String?,
    val image: URL?,
    val password: String?,
    val bio: String?,
)