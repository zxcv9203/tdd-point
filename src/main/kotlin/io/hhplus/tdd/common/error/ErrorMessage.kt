package io.hhplus.tdd.common.error

enum class ErrorMessage(
    val message: String,
) {
    INVALID_POINT_AMOUNT("충전 / 사용하는 포인트는 0 이상이어야 합니다."),
    MAX_POINT_EXCEEDED("최대 포인트를 초과했습니다."),
    INSUFFICIENT_POINT("포인트가 부족합니다."),

    INTERNAL_SERVER_ERROR("에러가 발생했습니다."),
}
