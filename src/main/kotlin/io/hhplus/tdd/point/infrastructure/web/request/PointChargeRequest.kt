package io.hhplus.tdd.point.infrastructure.web.request

import io.hhplus.tdd.common.ErrorMessage
import io.hhplus.tdd.point.domain.PointPolicy

data class PointChargeRequest(
    val amount: Long,
) {
    init {
        require(amount > PointPolicy.MIN_POINT) { ErrorMessage.POINT_AMOUNT_BELOW_MINIMUM.message }
        require(amount < PointPolicy.MAX_POINT) { ErrorMessage.MAX_POINT_EXCEEDED.message }
    }
}
