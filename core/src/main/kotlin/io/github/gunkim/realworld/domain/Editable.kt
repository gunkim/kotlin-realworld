package io.github.gunkim.realworld.domain

interface Editable<T> {
    fun edit(): T
}