package kim.hyunsub.division.controller

import kim.hyunsub.division.service.ReportService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/gatherings/{gatheringId}/report")
class ReportController(
	private val reportService: ReportService,
) {
	val log = KotlinLogging.logger { }

	@GetMapping("")
	fun report(@PathVariable gatheringId: String, leaderUserId: String): Any {
		return reportService.reportAll(gatheringId, leaderUserId)
	}
}
