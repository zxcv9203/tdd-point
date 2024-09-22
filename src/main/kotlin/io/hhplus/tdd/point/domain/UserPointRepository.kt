package io.hhplus.tdd.point.domain

interface UserPointRepository {
    fun save(userPoint: UserPoint): UserPoint

    fun getByUserId(userId: Long): UserPoint
}
