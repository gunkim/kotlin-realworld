package io.github.gunkim.realworld.application.user

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private const val TEST_USER_ID = 1L

@DisplayName("JwtProvider is")
class JwtProviderTest : StringSpec({
    val sut = JwtProvider()

    "should create JWT token successfully" {
        shouldNotThrow<IllegalStateException> {
            sut.create(TEST_USER_ID)
        }
    }

    "should parse JWT token correctly" {
        val jws = sut.create(TEST_USER_ID)
        sut.parse(jws) shouldBe TEST_USER_ID
    }
})