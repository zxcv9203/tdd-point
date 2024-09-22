package io.hhplus.tdd.point.stub

import io.hhplus.tdd.point.domain.UserPoint

object UserPointStub {
    fun create(point: Long): UserPoint =
        UserPoint(
            id = 1,
            point = point,
            updateMillis = System.currentTimeMillis(),
        )
}
