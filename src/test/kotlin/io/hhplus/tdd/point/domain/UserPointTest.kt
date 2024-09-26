package io.hhplus.tdd.point.domain

import io.hhplus.tdd.point.stub.UserPointStub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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

            assertThrows(IllegalArgumentException::class.java) {
                userPoint.charge(PointPolicy.MAX_POINT - 100)
            }
        }
    }
}
