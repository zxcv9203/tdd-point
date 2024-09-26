package io.hhplus.tdd.point.application

import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import io.hhplus.tdd.point.stub.PointHistoryStub
import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
@DisplayName("PointService 테스트")
class PointServiceTest {
    @InjectMocks
    private lateinit var pointService: PointService

    @Mock
    private lateinit var pointHistoryService: PointHistoryService

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

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        @Test
        @DisplayName("사용할 포인트보다 보유한 포인트가 많은 경우 포인트를 사용하고 사용 후 포인트를 반환합니다.")
        fun success() {
            val id = 1L
            val request = PointUseRequest(1000L)
            val want = UserPointStub.create(1000L)

            given(userPointService.use(id, request)).willReturn(want)

            val got = pointService.use(id, request)

            assertThat(got).isEqualTo(want)
        }
    }

    @Nested
    @DisplayName("포인트 조회")
    inner class GetById {
        @Test
        @DisplayName("전달한 아이디의 포인트 내역을 조회합니다.")
        fun success() {
            val id = 1L
            val want = UserPointStub.create(1000L)

            given(userPointService.getById(id)).willReturn(want)

            val got = pointService.getById(id)

            assertThat(got).isEqualTo(want)
        }
    }

    @Nested
    @DisplayName("포인트 충전 / 사용 이력")
    inner class FindHistoriesById {
        @Test
        @DisplayName("전달한 아이디의 포인트 충전 / 사용 이력을 조회합니다.")
        fun success() {
            val id = 1L
            val want = listOf(PointHistoryStub.createByUse(1000L, id))

            given(pointHistoryService.findHistoriesById(id)).willReturn(want)

            val got = pointService.findHistoriesById(id)

            assertThat(got).isEqualTo(want)
        }
    }
}
