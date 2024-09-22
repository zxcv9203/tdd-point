package io.hhplus.tdd.point.stub

import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.TransactionType

object PointHistoryStub {
    fun createByCharge(
        point: Long,
        userId: Long = 0,
        id: Long = 1,
    ): PointHistory =
        PointHistory(
            id = id,
            userId = userId,
            amount = point,
            type = TransactionType.CHARGE,
            timeMillis = 0,
        )
}
