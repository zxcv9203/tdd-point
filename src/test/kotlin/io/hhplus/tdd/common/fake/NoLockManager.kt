package io.hhplus.tdd.common.fake

import io.hhplus.tdd.common.lock.LockManager

class NoLockManager : LockManager {
    override fun <T> withLock(
        userId: Long,
        block: () -> T,
    ): T = block()
}
