package io.hhplus.tdd.common.error

enum class ErrorMessage(
    val message: String,
) {
    POINT_AMOUNT_BELOW_MINIMUM("충전 / 사용하는 포인트는 0 이상이어야 합니다."),
    MAX_POINT_EXCEEDED("최대 포인트를 초과했습니다."),
    INSUFFICIENT_POINT("포인트가 부족합니다."),

    INTERNAL_SERVER_ERROR("에러가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다. 요청을 다시 한번 확인해주세요."),
}
