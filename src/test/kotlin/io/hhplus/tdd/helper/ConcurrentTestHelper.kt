package io.hhplus.tdd.helper

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ConcurrentTestHelper {
    fun executeConcurrentTasks(
        numberOfThreads: Int,
        task: () -> Unit,
    ) {
        val latch = CountDownLatch(numberOfThreads)
        val executorService = Executors.newFixedThreadPool(numberOfThreads)

        repeat(numberOfThreads) {
            executorService.submit {
                try {
                    task()
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await(10, TimeUnit.SECONDS)
        executorService.shutdown()
    }
}
