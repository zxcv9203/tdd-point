package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.PointHistoryRepository
import org.springframework.stereotype.Service

@Service
class PointHistoryService(
    private val pointHistoryRepository: PointHistoryRepository,
) {
    fun save(history: PointHistory): PointHistory = pointHistoryRepository.save(history)

    fun findHistoriesById(id: Long): List<PointHistory> = pointHistoryRepository.findAllByUserId(id)
}
