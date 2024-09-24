package io.hhplus.tdd.point.infrastructure.web.request

import io.hhplus.tdd.common.error.ErrorMessage
import io.hhplus.tdd.point.domain.PointPolicy
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("PointUseRequest 테스트")
class PointUseRequestTest {
    @Nested
    @DisplayName("객체 생성")
    inner class Init {
        @ParameterizedTest(name = "포인트 사용 요청이 {0}인 경우 생성에 실패합니다.")
        @ValueSource(longs = [0L, -1L])
        @DisplayName("포인트 사용 요청이 0이하인 경우 생성에 실패합니다.")
        fun shouldFailWhenPointIsZeroOrLess(amount: Long) {
            assertThatThrownBy { PointUseRequest(amount) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.POINT_AMOUNT_BELOW_MINIMUM.message)
        }

        @ParameterizedTest(name = "포인트 사용 요청이 {0}인 경우 생성에 실패합니다.")
        @ValueSource(longs = [PointPolicy.MAX_POINT, Long.MAX_VALUE])
        @DisplayName("포인트 사용 요청이 최대 포인트 이상인 경우 생성에 실패합니다.")
        fun shouldFailWhenPointIsMoreThanMaxPoint(amount: Long) {
            assertThatThrownBy { PointUseRequest(amount) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(ErrorMessage.MAX_POINT_EXCEEDED.message)
        }
    }
}
