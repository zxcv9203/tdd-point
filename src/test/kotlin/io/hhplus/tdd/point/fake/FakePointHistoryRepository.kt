package io.hhplus.tdd.point.fake

import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.PointHistoryRepository

class FakePointHistoryRepository : PointHistoryRepository {
    private val table = mutableListOf<PointHistory>()

    override fun save(history: PointHistory): PointHistory {
        table.add(history)
        return history
    }
}
