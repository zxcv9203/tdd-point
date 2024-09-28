package io.hhplus.tdd.point.infrastructure.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.tdd.common.ErrorMessage
import io.hhplus.tdd.point.domain.PointHistoryRepository
import io.hhplus.tdd.point.domain.PointPolicy
import io.hhplus.tdd.point.domain.TransactionType
import io.hhplus.tdd.point.domain.UserPointRepository
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import io.hhplus.tdd.point.stub.PointHistoryStub
import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("PointController 테스트")
class IntegrationPointControllerTest(
    @Autowired
    private val objectMapper: ObjectMapper,
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val userPointRepository: UserPointRepository,
    @Autowired
    private val pointHistoryRepository: PointHistoryRepository,
) {
    @Nested
    @DisplayName("포인트 충전")
    inner class Charge {
        private val path = "/point/{id}/charge"

        @Test
        @DisplayName("포인트를 충전하면 충전된 포인트를 반환합니다.")
        fun success() {
            val id = 1L
            val point = 1000L
            val request = PointChargeRequest(point)

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(status().isOk)
                .andExpect(jsonPath("$.point").value(point))
            val histories = pointHistoryRepository.findAllByUserId(id)

            assertThat(histories).hasSize(1)
            assertThat(histories[0].amount).isEqualTo(point)
            assertThat(histories[0].userId).isEqualTo(id)
            assertThat(histories[0].type).isEqualTo(TransactionType.CHARGE)
        }

        @ParameterizedTest
        @ValueSource(longs = [PointPolicy.MIN_POINT, -1])
        @DisplayName("충전하려는 포인트가 최소 포인트 이하인 경우 400을 반환합니다.")
        fun pointAmountBelowMinimum(amount: Long) {
            val id = 1L
            val request =
                """
                {
                    "amount": $amount
                }
                """.trimIndent()

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request),
                ).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value(ErrorMessage.POINT_AMOUNT_BELOW_MINIMUM.message))
        }

        @ParameterizedTest
        @DisplayName("충전하려는 포인트가 포인트가 최대 포인트 이상인 경우 400을 반환합니다.")
        @ValueSource(longs = [PointPolicy.MAX_POINT, Long.MAX_VALUE])
        fun maxPointExceeded(amount: Long) {
            val id = 1L
            val request =
                """
                {
                    "amount": $amount
                }
                """.trimIndent()

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request),
                ).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value(ErrorMessage.MAX_POINT_EXCEEDED.message))
        }

        @Test
        @DisplayName("현재 보유하고 있는 포인트와 충전하는 포인트의 합계가 최대 포인트를 넘어가는 경우 400을 반환합니다.")
        fun totalPointsExceedMaximumLimit() {
            val id = 2L
            val request = PointChargeRequest(PointPolicy.MAX_POINT - 1000L)
            userPointRepository.save(UserPointStub.create(1000L, id))

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value(ErrorMessage.MAX_POINT_EXCEEDED.message))
        }
    }

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        private val path = "/point/{id}/use"

        @Test
        @DisplayName("사용할 포인트보다 보유한 포인트가 많은 경우 포인트를 사용하고 사용 후 포인트를 반환합니다.")
        fun success() {
            val id = 3L
            val want = 0L
            val request = PointUseRequest(1000L)
            userPointRepository.save(UserPointStub.create(1000L, id))

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(status().isOk)
                .andExpect(jsonPath("$.point").value(want))

            val histories = pointHistoryRepository.findAllByUserId(id)

            assertThat(histories).hasSize(1)
            assertThat(histories[0].amount).isEqualTo(1000L)
            assertThat(histories[0].userId).isEqualTo(id)
            assertThat(histories[0].type).isEqualTo(TransactionType.USE)
        }

        @ParameterizedTest
        @DisplayName("사용하려는 포인트가 최소 사용 포인트 이하인 경우 400을 반환합니다.")
        @ValueSource(longs = [PointPolicy.USE_MIN_POINT, -1])
        fun pointAmountBelowMinimum(amount: Long) {
            val id = 1L
            val request =
                """
                {
                    "amount": $amount
                }
                """

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request),
                ).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value(ErrorMessage.POINT_AMOUNT_BELOW_MINIMUM.message))
        }

        @ParameterizedTest
        @DisplayName("사용하려는 포인트가 최대 포인트보다 많은 경우 400을 반환합니다.")
        @ValueSource(longs = [PointPolicy.MAX_POINT, Long.MAX_VALUE])
        fun maxPointExceeded(amount: Long) {
            val id = 1L
            val request =
                """
                {
                    "amount": $amount
                }
                """

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request),
                ).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value(ErrorMessage.MAX_POINT_EXCEEDED.message))
        }

        @Test
        @DisplayName("사용하려는 포인트가 보유한 포인트보다 많은 경우 400을 반환합니다.")
        fun pointAmountExceedsCurrentPoint() {
            val id = 4L
            val request = PointUseRequest(1000L)

            mockMvc
                .perform(
                    patch(path, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value(ErrorMessage.INSUFFICIENT_POINT.message))
        }
    }

    @Nested
    @DisplayName("포인트 충전 / 사용 이력")
    inner class History {
        private val path = "/point/{id}/histories"

        @Test
        @DisplayName("전달한 아이디의 포인트 충전 / 사용 이력을 조회합니다.")
        fun success() {
            val id = 5L

            pointHistoryRepository.save(PointHistoryStub.createByUse(1000L, id))

            mockMvc
                .perform(
                    get(path, id),
                ).andExpect(status().isOk)
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[0].userId").value(id))
                .andExpect(jsonPath("$[0].type").value(TransactionType.USE.name))
        }
    }

    @Nested
    @DisplayName("포인트 조회")
    inner class Point {
        private val path = "/point/{id}"

        @Test
        @DisplayName("전달한 아이디의 포인트를 조회합니다.")
        fun success() {
            val id = 6L
            val want = 1000L
            userPointRepository.save(UserPointStub.create(1000L, id))

            mockMvc
                .perform(
                    get(path, id),
                ).andExpect(status().isOk)
                .andExpect(jsonPath("$.point").value(want))
        }
    }
}
