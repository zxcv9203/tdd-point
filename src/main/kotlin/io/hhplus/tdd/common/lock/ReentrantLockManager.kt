package io.hhplus.tdd.common.lock

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

@Component
class ReentrantLockManager : LockManager {
    private val locks = ConcurrentHashMap<Long, ReentrantLock>()

    override fun <T> withLock(
        id: Long,
        block: () -> T,
    ): T {
        val lock = locks.computeIfAbsent(id) { ReentrantLock() }
        lock.lock()
        return try {
            block()
        } finally {
            lock.unlock()
        }
    }
}
