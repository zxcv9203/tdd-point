package io.hhplus.tdd.common

interface LockManager {
    fun <T> withLock(block: () -> T): T
}
