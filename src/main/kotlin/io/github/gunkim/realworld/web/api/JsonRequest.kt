package io.github.gunkim.realworld.web.api

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonRequest(val value: String)