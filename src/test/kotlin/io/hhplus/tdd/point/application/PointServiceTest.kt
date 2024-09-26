package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
@DisplayName("PointService 테스트")
class PointServiceTest {
    @InjectMocks
    private lateinit var pointService: PointService

    @Mock
    private lateinit var userPointService: UserPointService

    @Nested
    @DisplayName("포인트 충전")
    inner class Charge {
        @Test
        @DisplayName("포인트를 충전하면 충전된 포인트를 반환합니다.")
        fun success() {
            val id = 1L
            val request = PointChargeRequest(1000L)
            val want = UserPointStub.create(1000L)

            given(userPointService.charge(id, request)).willReturn(want)

            val got = pointService.charge(id, request)

            assertThat(got).isEqualTo(want)
        }
    }
}
