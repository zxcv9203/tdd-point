package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.domain.UserPointRepository
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
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
}
