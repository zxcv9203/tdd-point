package io.hhplus.tdd.point.domain

data class PointHistory(
    val userId: Long,
    val type: TransactionType,
    val amount: Long,
    val timeMillis: Long,
    val id: Long = 0,
) {
    companion object {
        fun createByCharge(
            userId: Long,
            amount: Long,
        ): PointHistory =
            PointHistory(
                userId = userId,
                amount = amount,
                type = TransactionType.CHARGE,
                timeMillis = System.currentTimeMillis(),
            )
    }
}

/**
 * 포인트 트랜잭션 종류
 * - CHARGE : 충전
 * - USE : 사용
 */
enum class TransactionType {
    CHARGE,
    USE,
}
