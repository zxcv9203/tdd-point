package io.hhplus.tdd.common.lock

import io.hhplus.tdd.helper.ConcurrentTestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ReentrantLockManager 테스트")
class ReentrantLockManagerTest {
    private val reentrantLockManager: ReentrantLockManager = ReentrantLockManager()

    @Nested
    @DisplayName("실행하려는 블록을 thread safe 하게 실행")
    inner class WithLock {
        @Test
        @DisplayName("동시에 여러 쓰레드가 접근했을 때 thread safe하게 실행됩니다.")
        fun success() {
            val userId = 1L
            var got = 0L
            val want = 100L

            ConcurrentTestHelper.executeConcurrentTasks(100) {
                reentrantLockManager.withLock(userId) {
                    got++
                }
            }

            assertThat(got).isEqualTo(want)
        }
    }
}
