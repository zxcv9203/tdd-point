@file:Suppress("ktlint:standard:no-wildcard-imports")

package io.hhplus.tdd.point.infrastructure.web

import io.hhplus.tdd.point.application.PointService
import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
class PointController(
    private val pointService: PointService,
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("{id}")
    fun point(
        @PathVariable id: Long,
    ): UserPoint = pointService.getById(id)

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    fun history(
        @PathVariable id: Long,
    ): List<PointHistory> = emptyList()

    @PatchMapping("{id}/charge")
    fun charge(
        @PathVariable id: Long,
        @RequestBody request: PointChargeRequest,
    ): UserPoint = pointService.charge(id, request)

    @PatchMapping("{id}/use")
    fun use(
        @PathVariable id: Long,
        @RequestBody request: PointUseRequest,
    ): UserPoint = pointService.use(id, request)
}
