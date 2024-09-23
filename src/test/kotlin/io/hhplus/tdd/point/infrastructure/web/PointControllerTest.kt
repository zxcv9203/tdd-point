package io.hhplus.tdd.point.infrastructure.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.tdd.point.domain.PointHistoryRepository
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
class PointControllerTest(
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
                .andExpect(jsonPath("$.point").value(1000))
            val histories = pointHistoryRepository.findAllByUserId(id)

            assertThat(histories).hasSize(1)
            assertThat(histories[0].amount).isEqualTo(point)
            assertThat(histories[0].userId).isEqualTo(id)
            assertThat(histories[0].type).isEqualTo(TransactionType.CHARGE)
        }
    }

    @Nested
    @DisplayName("포인트 사용")
    inner class Use {
        private val path = "/point/{id}/use"

        @Test
        @DisplayName("사용할 포인트보다 보유한 포인트가 많은 경우 포인트를 사용하고 사용 후 포인트를 반환합니다.")
        fun success() {
            val id = 2L
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
    }

    @Nested
    @DisplayName("포인트 충전 / 사용 이력")
    inner class History {
        private val path = "/point/{id}/histories"

        @Test
        @DisplayName("전달한 아이디의 포인트 충전 / 사용 이력을 조회합니다.")
        fun success() {
            val id = 3L

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
            val id = 4L

            userPointRepository.save(UserPointStub.create(1000L, id))
            val want = UserPointStub.create(1000L)

            mockMvc
                .perform(
                    get(path, id),
                ).andExpect(status().isOk)
                .andExpect(jsonPath("$.point").value(want.point))
        }
    }
}
