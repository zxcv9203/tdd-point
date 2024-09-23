package io.hhplus.tdd.point.infrastructure.web.request

import io.hhplus.tdd.common.error.ErrorMessage
import io.hhplus.tdd.point.domain.PointPolicy
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("PointChargeRequest 테스트")
class PointChargeRequestTest {
    @Nested
    @DisplayName("포인트 충전 요청 생성")
    inner class Init {
        @ParameterizedTest(name = "포인트 사용 요청이 {0}인 경우 생성에 실패합니다.")
        @ValueSource(longs = [0L, -1L])
        @DisplayName("0 이하의 포인트가 주어지면 생성에 실패합니다.")
        fun lessZeroAmount(amount: Long) {
            assertThatThrownBy { PointChargeRequest(amount) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.INVALID_POINT_AMOUNT.message)
        }

        @ParameterizedTest(name = "포인트 사용 요청이 {0}인 경우 생성에 실패합니다.")
        @ValueSource(longs = [PointPolicy.MAX_POINT, Long.MAX_VALUE])
        @DisplayName("최대 포인트 이상으로 포인트가 주어지면 생성에 실패합니다.")
        fun moreThanMaxAmount(amount: Long) {
            assertThatThrownBy { PointChargeRequest(amount) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.MAX_POINT_EXCEEDED.message)
        }
    }
}
