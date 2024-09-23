package io.hhplus.tdd.common.lock

interface LockManager {
    fun <T> withLock(
        id: Long,
        block: () -> T,
    ): T
}
