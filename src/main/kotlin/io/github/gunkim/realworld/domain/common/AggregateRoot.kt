package io.github.gunkim.realworld.domain.common

import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents

abstract class AggregateRoot<T : DomainEntity<T, TID>?, TID> : DomainEntity<T, TID>() {
    @Transient
    private val domainEvents: MutableList<Any?> = ArrayList()

    protected fun registerEvent(event: T) {
        requireNotNull(event) { "Domain event must not be null" }
        domainEvents.add(event)
    }

    @AfterDomainEventPublication
    protected fun clearDomainEvents() {
        domainEvents.clear()
    }

    @DomainEvents
    protected fun domainEvents(): Collection<Any> {
        return listOf(domainEvents)
    }
}