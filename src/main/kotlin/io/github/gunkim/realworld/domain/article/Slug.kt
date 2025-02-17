package io.github.gunkim.realworld.domain.article

import java.util.UUID

data class Slug internal constructor(
    val value: String,
) {
    companion object {
        private const val SPECIAL_CHARS_PATTERN = "[^a-z0-9\\s-]"
        private const val WHITESPACE_PATTERN = "\\s+"
        private const val MULTIPLE_HYPHENS_PATTERN = "-+"
        private const val RANDOM_STRING_LENGTH = 8
        private const val HYPHEN = "-"

        fun fromTitle(raw: String): Slug {
            val sanitizedSlug = sanitizeString(raw)
            val randomSuffix = generateRandomSuffix()
            return Slug("$sanitizedSlug$HYPHEN$randomSuffix")
        }

        private fun sanitizeString(input: String): String = input
            .lowercase()
            .replace(Regex(SPECIAL_CHARS_PATTERN), "")
            .replace(Regex(WHITESPACE_PATTERN), HYPHEN)
            .replace(Regex(MULTIPLE_HYPHENS_PATTERN), HYPHEN)
            .trim(HYPHEN[0])

        private fun generateRandomSuffix(): String =
            UUID.randomUUID().toString().take(RANDOM_STRING_LENGTH)
    }

    override fun toString() = value
}