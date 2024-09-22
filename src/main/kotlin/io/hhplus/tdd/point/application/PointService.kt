package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointHistoryService: PointHistoryService,
    private val userPointService: UserPointService,
) {
    fun charge(
        userId: Long,
        request: PointChargeRequest,
    ): UserPoint =
        userPointService
            .charge(userId, request)
            .apply { pointHistoryService.save(PointHistory.createByCharge(userId, request.amount)) }
}
