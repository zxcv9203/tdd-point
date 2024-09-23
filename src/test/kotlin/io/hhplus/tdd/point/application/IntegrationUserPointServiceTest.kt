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
@DisplayName("UserPointService 통합 테스트")
class IntegrationUserPointServiceTest(
    @Autowired
    private val userPointService: UserPointService,
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

            ConcurrentTestHelper.executeConcurrentTasks(99) { userPointService.charge(id, request) }
            val got = userPointService.charge(id, request).point

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
            userPointService.charge(id, chargeRequest)
        }

        @Test
        @DisplayName("동시에 여러 쓰레드가 접근하는 경우 포인트가 정상 사용된다.")
        fun success() {
            val useRequest = PointUseRequest(100L)
            val want = 0L

            ConcurrentTestHelper.executeConcurrentTasks(99) {
                userPointService.use(id, useRequest)
            }
            val got = userPointService.use(id, useRequest).point

            assertThat(got).isEqualTo(want)
        }
    }
}
