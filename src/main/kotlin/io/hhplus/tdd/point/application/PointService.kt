package io.hhplus.tdd.point.application

import io.hhplus.tdd.common.lock.LockManager
import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointHistoryService: PointHistoryService,
    private val userPointService: UserPointService,
    private val lockManager: LockManager,
) {
    fun charge(
        id: Long,
        request: PointChargeRequest,
    ): UserPoint =
        lockManager.withLock(id) {
            userPointService
                .charge(id, request)
                .also { pointHistoryService.save(PointHistory.createByCharge(id, request.amount)) }
        }

    fun getById(id: Long): UserPoint = userPointService.getById(id)

    fun use(
        id: Long,
        request: PointUseRequest,
    ): UserPoint =
        lockManager.withLock(id) {
            userPointService
                .use(id, request)
                .also { pointHistoryService.save(PointHistory.createByUse(id, request.amount)) }
        }

    fun findHistoriesById(id: Long): List<PointHistory> = pointHistoryService.findHistoriesById(id)
}
