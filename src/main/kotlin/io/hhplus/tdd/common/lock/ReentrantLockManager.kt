package io.hhplus.tdd.common.lock

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

@Component
class ReentrantLockManager : LockManager {
    private val locks = ConcurrentHashMap<Long, ReentrantLock>()

    override fun <T> withLock(
        userId: Long,
        block: () -> T,
    ): T {
        val lock = locks.computeIfAbsent(userId) { ReentrantLock() }
        lock.lock()
        return try {
            block()
        } finally {
            lock.unlock()
        }
    }
}
