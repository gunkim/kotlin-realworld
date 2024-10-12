package io.github.gunkim.realworld.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.spec.SecretKeySpec

@Component
class SymmetricKeyProvider(
    @Value("\${jwt.secret.key}")
    private val symmetricKey: String,
) {
    val key: SecretKeySpec = SecretKeySpec(symmetricKey.toByteArray(), "HmacSHA256")
}