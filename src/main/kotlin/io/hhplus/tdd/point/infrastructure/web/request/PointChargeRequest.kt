package io.hhplus.tdd.point.infrastructure.web.request

import io.hhplus.tdd.common.error.ErrorMessage
import io.hhplus.tdd.point.domain.PointPolicy

data class PointChargeRequest(
    val amount: Long,
) {
    init {
        require(amount > PointPolicy.MIN_POINT) { ErrorMessage.INVALID_POINT_AMOUNT.message }
        require(amount < PointPolicy.MAX_POINT) { ErrorMessage.MAX_POINT_EXCEEDED.message }
    }
}
