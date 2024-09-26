package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.domain.UserPointRepository
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import org.springframework.stereotype.Service

@Service
class UserPointService(
    private val userPointRepository: UserPointRepository,
) {
    fun charge(
        id: Long,
        request: PointChargeRequest,
    ): UserPoint =
        userPointRepository
            .getByUserId(id)
            .apply { charge(request.amount) }
            .also { userPointRepository.save(it) }

    fun use(
        id: Long,
        request: PointUseRequest,
    ): UserPoint =
        userPointRepository
            .getByUserId(id)
            .apply { use(request.amount) }
            .also { userPointRepository.save(it) }

    fun getById(id: Long): UserPoint = userPointRepository.getByUserId(id)
}
