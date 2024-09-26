package io.hhplus.tdd.point.infrastructure.persistence

import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.domain.UserPointRepository
import org.springframework.stereotype.Repository

@Repository
class InMemoryUserPointRepository(
    private val userPointTable: UserPointTable,
) : UserPointRepository {
    override fun save(userPoint: UserPoint): UserPoint = userPointTable.insertOrUpdate(userPoint.id, userPoint.point)

    override fun getByUserId(userId: Long): UserPoint = userPointTable.selectById(userId)
}
