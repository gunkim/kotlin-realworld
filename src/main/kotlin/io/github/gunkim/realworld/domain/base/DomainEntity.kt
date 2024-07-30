package io.github.gunkim.realworld.domain.base

abstract class DomainEntity<T : DomainEntity<T, TID>?, TID> {
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        return equals(other as T)
    }

    fun equals(other: T): Boolean {
        if (other == null) {
            return false
        }

        if (id == null) {
            return false
        }

        if (other.javaClass == javaClass) {
            return id == other.id
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return if (id == null) 0 else id.hashCode()
    }

    abstract val id: TID?
}