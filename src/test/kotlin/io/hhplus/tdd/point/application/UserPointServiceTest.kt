package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.infrastructure.persistence.FakeUserPointRepository
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UserPointService 테스트")
class UserPointServiceTest {
    private val userPointRepository = FakeUserPointRepository()
    private val userPointService = UserPointService(userPointRepository = userPointRepository)

    private val userPoint = UserPointStub.create(100L)

    @BeforeEach
    fun setUp() {
        userPointRepository.save(userPoint)
    }

    @Nested
    @DisplayName("포인트 충전")
    inner class Charge {
        @Test
        @DisplayName("현재 포인트와 충전하려는 포인트의 합계가 포인트 소지한도 이내인 경우 충전된 포인트를 반환합니다.")
        fun success() {
            val userId = 1L
            val request = PointChargeRequest(100L)
            val want = 200L

            val got = userPointService.charge(userId, request)

            assertThat(got.point).isEqualTo(want)
        }
    }

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        @Test
        @DisplayName("사용하려는 포인트보다 보유한 포인트가 많은 경우 포인트 사용에 결과를 반환합니다.")
        fun success() {
            val userId = 1L
            val request = PointUseRequest(100L)
            val want = 0L

            val got = userPointService.use(userId, request).point

            assertThat(got).isEqualTo(want)
        }
    }
}
