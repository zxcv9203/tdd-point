package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointHistoryService: PointHistoryService,
    private val userPointService: UserPointService,
) {
    fun charge(
        id: Long,
        request: PointChargeRequest,
    ): UserPoint =
        userPointService
            .charge(id, request)
            .also { pointHistoryService.save(PointHistory.createByCharge(id, request.amount)) }

    fun use(
        id: Long,
        request: PointUseRequest,
    ): UserPoint =
        userPointService
            .use(id, request)
            .also { pointHistoryService.save(PointHistory.createByUse(id, request.amount)) }

    fun getById(id: Long): UserPoint = userPointService.getById(id)
}
