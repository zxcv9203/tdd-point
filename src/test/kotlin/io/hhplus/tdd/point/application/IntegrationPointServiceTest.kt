package io.hhplus.tdd.point.application

import io.hhplus.tdd.helper.ConcurrentTestHelper
import io.hhplus.tdd.point.domain.PointHistoryRepository
import io.hhplus.tdd.point.domain.UserPointRepository
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("PointService 통합 테스트")
class IntegrationPointServiceTest(
    @Autowired
    private val pointService: PointService,
    @Autowired
    private val userPointRepository: UserPointRepository,
    @Autowired
    private val pointHistoryRepository: PointHistoryRepository,
) {
    @Nested
    @DisplayName("포인트 충전")
    inner class Charge {
        @Test
        @DisplayName("동시에 여러 쓰레드가 접근하는 경우 포인트가 정상 충전된다.")
        fun success() {
            val id = 1L
            val request = PointChargeRequest(100L)
            val want = 10000L

            ConcurrentTestHelper.executeConcurrentTasks(99) { pointService.charge(id, request) }
            val got = pointService.charge(id, request).point

            assertThat(got).isEqualTo(want)
            assertThat(pointHistoryRepository.findAllByUserId(id)).hasSize(100)
        }
    }

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        @Test
        @DisplayName("동시에 여러 쓰레드가 접근하는 경우 포인트가 정상 사용된다.")
        fun success() {
            val id = 2L
            val useRequest = PointUseRequest(100L)
            val want = 0L

            userPointRepository.save(UserPointStub.create(10000L, id))

            ConcurrentTestHelper.executeConcurrentTasks(99) {
                pointService.use(id, useRequest)
            }
            val got = pointService.use(id, useRequest).point

            assertThat(got).isEqualTo(want)
            assertThat(pointHistoryRepository.findAllByUserId(id)).hasSize(100)
        }
    }

    @Nested
    @DisplayName("포인트 충전, 사용이 동시에 발생하는 경우")
    inner class ChargeAndUse {
        @Test
        @DisplayName("같은 사용자가 동시에 여러 쓰레드가 접근하는 경우 충전과 사용이 정상적으로 처리된다.")
        fun success() {
            val id = 3L
            val chargeRequest = PointChargeRequest(100L)
            val useRequest = PointUseRequest(100L)
            val want = 9900L

            userPointRepository.save(UserPointStub.create(10000L, id))

            ConcurrentTestHelper.executeConcurrentTasks(99) {
                pointService.charge(id, chargeRequest)
                pointService.use(id, useRequest)
            }
            val got = pointService.use(id, useRequest).point

            assertThat(got).isEqualTo(want)
            assertThat(pointHistoryRepository.findAllByUserId(id)).hasSize(199)
        }

        @Test
        @DisplayName("서로 다른 사용자가 동시에 충전 / 사용을 진행하는 경우 정상적으로 처리된다.")
        fun success2() {
            val user1 = 4L
            val user2 = 5L
            val chargeRequest = PointChargeRequest(100L)
            val useRequest = PointUseRequest(100L)
            val want = 9900L

            userPointRepository.save(UserPointStub.create(10000L, user1))
            userPointRepository.save(UserPointStub.create(10000L, user2))

            ConcurrentTestHelper.executeConcurrentTasks(99) {
                pointService.charge(user1, chargeRequest)
                pointService.use(user1, useRequest)
                pointService.charge(user2, chargeRequest)
                pointService.use(user2, useRequest)
            }
            val user1Point = pointService.use(user1, useRequest).point
            val user2Point = pointService.use(user2, useRequest).point

            assertThat(user1Point).isEqualTo(want)
            assertThat(user2Point).isEqualTo(want)
            assertThat(pointHistoryRepository.findAllByUserId(user1)).hasSize(199)
            assertThat(pointHistoryRepository.findAllByUserId(user2)).hasSize(199)
        }
    }
}
