package io.hhplus.tdd.point.application

import io.hhplus.tdd.common.error.ErrorMessage
import io.hhplus.tdd.point.domain.PointPolicy
import io.hhplus.tdd.point.fake.FakeUserPointRepository
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UserPointService 테스트")
class UserPointServiceTest {
    private val userPointRepository = FakeUserPointRepository()
    private val userPointService =
        UserPointService(
            userPointRepository = userPointRepository,
        )
    private val userPoint = UserPointStub.create(100L)

    @BeforeEach
    fun setUp() {
        userPointRepository.save(userPoint)
    }

    @Nested
    @DisplayName("포인트 충전")
    inner class Charge {
        @Test
        @DisplayName("현재 포인트와 충전하려는 포인트의 합계가 포인트 소지한도 이내인 경우 충전에 성공합니다.")
        fun success() {
            val userId = 1L
            val request = PointChargeRequest(100L)
            val want = 200L

            val got = userPointService.charge(userId, request)

            assertThat(got.point).isEqualTo(want)
        }

        @Test
        @DisplayName("최대 포인트를 넘어가는 상황인 경우 포인트 충전에 실패합니다.")
        fun fail() {
            val userId = 1L
            val request = PointChargeRequest(PointPolicy.MAX_POINT - 1)

            assertThatThrownBy { userPointService.charge(userId, request) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.MAX_POINT_EXCEEDED.message)
        }
    }

    @Nested
    @DisplayName("포인트 조회")
    inner class GetById {
        @Test
        @DisplayName("사용자 ID로 포인트를 조회합니다.")
        fun success() {
            val userId = 1L
            val want = userPoint.point

            val got = userPointService.getById(userId).point

            assertThat(got).isEqualTo(want)
        }
    }

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        @Test
        @DisplayName("사용하려는 포인트보다 보유한 포인트가 많은 경우 포인트 사용에 성공합니다.")
        fun success() {
            val userId = 1L
            val request = PointUseRequest(50L)
            val want = 50L

            val got = userPointService.use(userId, request).point

            assertThat(got).isEqualTo(want)
        }

        @Test
        @DisplayName("사용하려는 포인트가 보유한 포인트보다 많은 경우 포인트 사용에 실패합니다.")
        fun fail() {
            val userId = 1L
            val request = PointUseRequest(101L)

            assertThatThrownBy { userPointService.use(userId, request) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.INSUFFICIENT_POINT.message)
        }
    }
}
