package io.hhplus.tdd.common

import org.springframework.stereotype.Component
import java.util.concurrent.locks.ReentrantLock

@Component
class ReentrantLockManager : LockManager {
    private val lock = ReentrantLock()

    override fun <T> withLock(block: () -> T): T {
        lock.lock()
        return try {
            block()
        } finally {
            lock.unlock()
        }
    }
}
