package io.hhplus.tdd.common.fake

import io.hhplus.tdd.common.lock.LockManager

class NoLockManager : LockManager {
    override fun <T> withLock(
        id: Long,
        block: () -> T,
    ): T = block()
}
