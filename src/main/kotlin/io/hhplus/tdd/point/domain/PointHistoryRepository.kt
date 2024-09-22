package io.hhplus.tdd.point.domain

interface PointHistoryRepository {
    fun save(history: PointHistory): PointHistory
}
