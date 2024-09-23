package io.hhplus.tdd.point.application

import io.hhplus.tdd.common.lock.LockManager
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.domain.UserPointRepository
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import org.springframework.stereotype.Service

@Service
class UserPointService(
    private val lockManager: LockManager,
    private val userPointRepository: UserPointRepository,
) {
    fun charge(
        id: Long,
        request: PointChargeRequest,
    ): UserPoint =
        lockManager.withLock(id) {
            userPointRepository
                .getByUserId(id)
                .apply { charge(request.amount) }
                .also { userPointRepository.save(it) }
        }

    fun getById(id: Long): UserPoint = userPointRepository.getByUserId(id)
}
