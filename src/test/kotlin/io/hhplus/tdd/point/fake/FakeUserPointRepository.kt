package io.hhplus.tdd.point.fake

import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.domain.UserPointRepository

class FakeUserPointRepository : UserPointRepository {
    private val userPoints = mutableMapOf<Long, UserPoint>()

    override fun save(userPoint: UserPoint): UserPoint {
        userPoints[userPoint.id] = userPoint
        return userPoint
    }

    override fun getByUserId(userId: Long): UserPoint =
        userPoints[userId] ?: UserPoint(id = userId, point = 0, updateMillis = System.currentTimeMillis())
}
