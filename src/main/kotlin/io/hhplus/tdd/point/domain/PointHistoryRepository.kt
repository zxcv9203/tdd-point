package io.hhplus.tdd.point.domain

interface PointHistoryRepository {
    fun save(history: PointHistory): PointHistory

    fun findAllByUserId(id: Long): List<PointHistory>
}
