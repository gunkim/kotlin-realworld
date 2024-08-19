package io.github.gunkim.realworld.config.request

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonRequest(val value: String)