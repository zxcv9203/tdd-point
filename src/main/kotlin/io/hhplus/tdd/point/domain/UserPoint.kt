package io.hhplus.tdd.point.domain

import io.hhplus.tdd.common.ErrorMessage

data class UserPoint(
    val id: Long,
    var point: Long,
    val updateMillis: Long,
) {
    fun charge(amount: Long) {
        require(this.point + amount < PointPolicy.MAX_POINT) { ErrorMessage.MAX_POINT_EXCEEDED.message }
        this.point += amount
    }

    fun use(amount: Long) {
        require(this.point - amount >= PointPolicy.USE_MIN_POINT) { ErrorMessage.INSUFFICIENT_POINT.message }
        this.point -= amount
    }
}
