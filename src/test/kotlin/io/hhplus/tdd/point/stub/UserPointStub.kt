package io.hhplus.tdd.point.stub

import io.hhplus.tdd.point.domain.UserPoint

object UserPointStub {
    fun create(
        point: Long,
        id: Long = 1,
    ): UserPoint =
        UserPoint(
            id = id,
            point = point,
            updateMillis = System.currentTimeMillis(),
        )
}
