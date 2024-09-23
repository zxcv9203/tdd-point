package io.hhplus.tdd.point.infrastructure.web.request

import io.hhplus.tdd.common.error.ErrorMessage
import io.hhplus.tdd.point.domain.PointPolicy

data class PointUseRequest(
    val amount: Long,
) {
    init {
        require(amount > 0) { ErrorMessage.INVALID_POINT_AMOUNT.message }
        require(amount < PointPolicy.MAX_POINT) { ErrorMessage.MAX_POINT_EXCEEDED.message }
    }
}
