package io.hhplus.tdd.point.domain

import io.hhplus.tdd.common.ErrorMessage
import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UserPoint 테스트")
class UserPointTest {
    @Nested
    @DisplayName("포인트 충전")
    inner class Charge {
        @Test
        @DisplayName("현재 포인트와 충전하려는 포인트의 합계가 포인트 소지한도 이내인 경우 충전에 성공합니다.")
        fun success() {
            val want = 200L
            val userPoint = UserPointStub.create(100L)

            userPoint.charge(100L)

            assertThat(userPoint.point).isEqualTo(want)
        }

        @Test
        @DisplayName("충전시 최대 포인트를 넘어가는 상황인 경우 포인트 충전에 실패합니다.")
        fun fail() {
            val userPoint = UserPointStub.create(100)

            assertThatThrownBy { userPoint.charge(PointPolicy.MAX_POINT - 100) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.MAX_POINT_EXCEEDED.message)
        }
    }

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        @Test
        @DisplayName("사용하려는 포인트보다 보유한 포인트가 많은 경우 포인트 사용에 성공합니다.")
        fun success() {
            val want = 50L
            val userPoint = UserPointStub.create(100L)

            userPoint.use(50L)

            assertThat(userPoint.point).isEqualTo(want)
        }

        @Test
        @DisplayName("사용하려는 포인트가 보유한 포인트보다 많은 경우 포인트 사용에 실패합니다.")
        fun fail() {
            val userPoint = UserPointStub.create(PointPolicy.USE_MIN_POINT)

            assertThatThrownBy { userPoint.use(PointPolicy.USE_MIN_POINT + 1) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.INSUFFICIENT_POINT.message)
        }
    }
}
