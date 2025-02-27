package io.github.gunkim.realworld.domain.auth.service

interface UserPasswordService {
    fun matches(rawPassword: String, encodedPassword: String): Boolean
    fun encode(rawPassword: String): String
}