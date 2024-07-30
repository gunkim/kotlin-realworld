package io.github.gunkim.realworld.domain.base

import java.lang.reflect.Field
import java.util.*

abstract class ValueObject<T : ValueObject<T>?> {
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other.javaClass != javaClass) {
            return false
        }

        return equals(other as T)
    }

    fun equals(other: T): Boolean {
        if (other == null) {
            return false
        }

        return equalityFields.contentEquals(other.equalityFields)
    }

    override fun hashCode(): Int {
        var hash = 17
        for (each in equalityFields) {
            hash = hash * 31 + (each?.hashCode() ?: 0)
        }
        return hash
    }

    protected val equalityFields: Array<Any>
        get() = Arrays.stream(javaClass.declaredFields)
            .map { field: Field ->
                try {
                    field.isAccessible = true
                    return@map field[this]
                } catch (e: IllegalAccessException) {
                    throw RuntimeException(e)
                }
            }
            .toArray()
}