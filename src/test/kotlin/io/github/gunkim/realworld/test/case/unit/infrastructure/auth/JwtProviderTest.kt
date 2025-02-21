package io.github.gunkim.realworld.test.case.unit.infrastructure.auth

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.auth.JwtProvider
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.UUID
import javax.crypto.spec.SecretKeySpec

private val key = SecretKeySpec(UUID.randomUUID().toString().toByteArray(), "HmacSHA256")

private val TEST_USER_ID = UserId.create()


@DisplayName("JwtProvider is")
class JwtProviderTest : StringSpec({
    val sut = JwtProvider(key)

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