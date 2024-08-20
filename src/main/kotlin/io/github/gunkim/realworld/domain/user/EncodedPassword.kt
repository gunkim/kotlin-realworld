package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

/**
 * Represents an encoded password.
 *
 * This class is an embeddable value object that holds an encoded password. It is used in the context of user authentication
 * and password management. The password is stored as a string value with a minimum length of 8 characters.
 *
 * @property value The encoded password value.
 * @constructor Creates an instance of the `EncodedPassword` class with the specified encoded password value.
 * @throws IllegalArgumentException if the encoded password value has a length less than 8 characters.
 */
@Embeddable
class EncodedPassword private constructor(
    @Column(name = "password")
    val value: String,
) : ValueObject<EncodedPassword>() {
    init {
        require(value.length >= 8) { "Password must be at least 8 characters" }
    }

    companion object {
        fun of(value: String, encoder: (String) -> String) = EncodedPassword(encoder(value))
    }
}
