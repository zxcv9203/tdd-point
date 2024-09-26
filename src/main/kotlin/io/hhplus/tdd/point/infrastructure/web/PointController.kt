package io.hhplus.tdd.point.infrastructure.web

import io.hhplus.tdd.point.application.PointService
import io.hhplus.tdd.point.domain.PointHistory
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.infrastructure.web.request.PointChargeRequest
import io.hhplus.tdd.point.infrastructure.web.request.PointUseRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/point")
class PointController(
    private val pointService: PointService,
) {
    @GetMapping("{id}")
    fun point(
        @PathVariable id: Long,
    ): UserPoint = pointService.getById(id)

    @GetMapping("{id}/histories")
    fun history(
        @PathVariable id: Long,
    ): List<PointHistory> = pointService.findHistoriesById(id)

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
