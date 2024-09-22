package io.hhplus.tdd.point.infrastructure.web.request

import io.hhplus.tdd.common.error.ErrorMessage
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("PointChargeRequest 테스트")
class PointChargeRequestTest {
    @Nested
    @DisplayName("포인트 충전 요청 생성")
    inner class Init {
        @Test
        @DisplayName("최대 포인트 이하의 양수의 포인트가 주어지면 생성에 성공합니다.")
        fun success() {
            val want = 100L

            val got = PointChargeRequest(want).amount

            assertThat(got).isEqualTo(want)
        }

        @Test
        @DisplayName("0 이하의 포인트가 주어지면 생성에 실패합니다.")
        fun lessZeroAmount() {
            assertThatThrownBy { PointChargeRequest(0) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.INVALID_POINT_AMOUNT.message)
        }

        @Test
        @DisplayName("최대 포인트 이상으로 포인트가 주어지면 생성에 실패합니다.")
        fun moreThanMaxAmount() {
            assertThatThrownBy { PointChargeRequest(Long.MAX_VALUE) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.MAX_POINT_EXCEEDED.message)
        }
    }
}
