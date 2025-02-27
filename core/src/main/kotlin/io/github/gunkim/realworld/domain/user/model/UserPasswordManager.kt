package io.github.gunkim.realworld.domain.user.model

interface UserPasswordManager {
    fun matches(rawPassword: String, encodedPassword: String): Boolean
    fun encode(rawPassword: String): String
}