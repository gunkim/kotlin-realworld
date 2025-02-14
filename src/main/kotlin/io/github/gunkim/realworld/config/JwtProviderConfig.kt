package io.github.gunkim.realworld.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtProviderConfig {
    @Bean
    fun secretKeySpec(
        @Value("\${jwt.secret.key}")
        symmetricKey: String,
    ): SecretKeySpec = SecretKeySpec(symmetricKey.toByteArray(), ALGORITHM)

    companion object {
        private const val ALGORITHM = "HmacSHA256"
    }
}