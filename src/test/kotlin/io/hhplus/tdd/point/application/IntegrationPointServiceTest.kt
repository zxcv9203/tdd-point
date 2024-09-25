package io.hhplus.tdd.point.application

import io.hhplus.tdd.helper.ConcurrentTestHelper
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
        }
    }

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        private val id = 2L

        @BeforeEach
        fun setUp() {
            val chargeRequest = PointChargeRequest(10000L)
            pointService.charge(id, chargeRequest)
        }

        @Test
        @DisplayName("동시에 여러 쓰레드가 접근하는 경우 포인트가 정상 사용된다.")
        fun success() {
            val useRequest = PointUseRequest(100L)
            val want = 0L

            ConcurrentTestHelper.executeConcurrentTasks(99) {
                pointService.use(id, useRequest)
            }
            val got = pointService.use(id, useRequest).point

            assertThat(got).isEqualTo(want)
        }
    }

    @Nested
    @DisplayName("포인트 충전, 사용이 동시에 발생하는 경우")
    inner class ChargeAndUse {
        private val id = 3L

        @BeforeEach
        fun setUp() {
            val chargeRequest = PointChargeRequest(10000L)
            pointService.charge(id, chargeRequest)
        }

        @Test
        @DisplayName("동시에 여러 쓰레드가 접근하는 경우 충전과 사용이 정상적으로 처리된다.")
        fun success() {
            val chargeRequest = PointChargeRequest(100L)
            val useRequest = PointUseRequest(100L)
            val want = 9900L

            ConcurrentTestHelper.executeConcurrentTasks(99) {
                pointService.charge(id, chargeRequest)
                pointService.use(id, useRequest)
            }
            val got = pointService.use(id, useRequest).point

            assertThat(got).isEqualTo(want)
        }
    }
}
