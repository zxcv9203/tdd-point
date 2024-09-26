package io.hhplus.tdd.common.fake

import io.hhplus.tdd.common.LockManager

class NoLockManager : LockManager {
    override fun <T> withLock(block: () -> T): T = block()
}
