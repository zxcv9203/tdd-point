package io.hhplus.tdd.point.infrastructure.persistence

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.PointHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class InMemoryPointHistoryRepository(
    private val pointHistoryTable: PointHistoryTable,
) : PointHistoryRepository {
    override fun save(history: PointHistory): PointHistory =
        pointHistoryTable.insert(
            id = history.userId,
            amount = history.amount,
            transactionType = history.type,
            updateMillis = history.timeMillis,
        )

    override fun findAllByUserId(id: Long): List<PointHistory> = pointHistoryTable.selectAllByUserId(id)
}
