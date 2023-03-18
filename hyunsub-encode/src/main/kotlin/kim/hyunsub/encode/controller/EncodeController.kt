package kim.hyunsub.encode.controller

import kim.hyunsub.common.api.model.EncodeParams
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.encode.model.EncodeStatus
import kim.hyunsub.encode.service.EncodeService
import mu.KotlinLogging
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/encode")
class EncodeController(
	private val encodeService: EncodeService,
	private val sink: Sinks.Many<EncodeStatus>,
) {
	private val log = KotlinLogging.logger { }

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
