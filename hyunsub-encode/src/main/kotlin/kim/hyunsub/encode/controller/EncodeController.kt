package kim.hyunsub.encode.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.encode.model.EncodeParams
import kim.hyunsub.encode.model.EncodeStatus
import kim.hyunsub.encode.service.EncodeService
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/encode")
class EncodeController(
	private val encodeService: EncodeService,
	private val sink: Sinks.Many<EncodeStatus>,
) {
	companion object : Log

	@PostMapping("")
	fun encode(@RequestBody params: EncodeParams): SimpleResponse {
		log.info("[EncodeController] encode: params={}", params)
		encodeService.pushToQueue(params)
		return SimpleResponse()
	}

	@GetMapping("/status")
	fun status(res: HttpServletResponse): Flux<ServerSentEvent<EncodeStatus>> {
		res.setHeader("X-Accel-Buffering", "no")

		return sink.asFlux()
			.map { ServerSentEvent.builder<EncodeStatus>().data(it).build() }
	}
}
