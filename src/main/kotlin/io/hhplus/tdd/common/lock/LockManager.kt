package io.hhplus.tdd.common.lock

interface LockManager {
    fun <T> withLock(
        userId: Long,
        block: () -> T,
    ): T
}
