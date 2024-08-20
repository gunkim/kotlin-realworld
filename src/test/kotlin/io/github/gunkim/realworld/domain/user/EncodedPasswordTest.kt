package io.github.gunkim.realworld.domain.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@DisplayName("EncodedPassword is")
class EncodedPasswordTest : StringSpec({
    "should create an EncodedPassword instance with a password of at least 8 characters length" {
        val encoder: (String) -> String = String::uppercase
        val password = "securepass"
        val encodedPassword = EncodedPassword.of(password, encoder)
        encodedPassword.value shouldBe password.uppercase()
    }

    "should fail to create an EncodedPassword instance when the length of the password is less than 8 characters" {
        val encoder: (String) -> String = String::uppercase
        val password = "short"
        shouldThrow<IllegalArgumentException> {
            EncodedPassword.of(password, encoder)
        }.message shouldBe "Password must be at least 8 characters"
    }

    "should apply the provided encoder function to the password value" {
        val encoder: (String) -> String = { it.reversed() }
        val password = "securepass"
        val encodedPassword = EncodedPassword.of(password, encoder)
        encodedPassword.value shouldBe password.reversed()
    }
})