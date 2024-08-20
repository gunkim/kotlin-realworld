package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.UserId
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

private val keyPair: KeyPair = generateKeyPair()
private val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey
private val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey

private val TEST_USER_ID = UserId(UUID.randomUUID())

private fun generateKeyPair(): KeyPair {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }
    return keyPairGenerator.generateKeyPair()
}

@DisplayName("JwtProvider is")
class JwtProviderTest : StringSpec({
    val sut = JwtProvider(privateKey, publicKey)

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