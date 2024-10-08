package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.infrastructure.persistence.FakePointHistoryRepository
import io.hhplus.tdd.point.stub.PointHistoryStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("PointHistoryService 테스트")
class PointHistoryServiceTest {
    private val pointHistoryRepository = FakePointHistoryRepository()
    private val pointHistoryService = PointHistoryService(pointHistoryRepository)

    @Nested
    @DisplayName("포인트 이력 저장")
    inner class Save {
        @Test
        @DisplayName("포인트 이력을 저장하면 저장된 이력을 반환합니다.")
        fun save() {
            val want = PointHistoryStub.createByCharge(100L, 1L)

            val got = pointHistoryService.save(PointHistoryStub.createByCharge(100L, 1L))

            assertThat(got).isEqualTo(want)
        }
    }

    @Nested
    @DisplayName("포인트 이력 조회")
    inner class FindHistoriesById {
        @BeforeEach
        fun setUp() {
            pointHistoryRepository.save(PointHistoryStub.createByCharge(100L, 1L))
            pointHistoryRepository.save(PointHistoryStub.createByUse(50L, 1L, 2))
        }

        @Test
        @DisplayName("사용자 ID로 포인트 이력을 조회하면 해당 사용자의 모든 포인트 이력을 반환합니다.")
        fun findHistoriesById() {
            val userId = 1L
            val want =
                listOf(
                    PointHistoryStub.createByCharge(100L, userId),
                    PointHistoryStub.createByUse(50L, userId, 2),
                )

            val got = pointHistoryService.findHistoriesById(userId)

            assertThat(got).isEqualTo(want)
        }
    }
}
