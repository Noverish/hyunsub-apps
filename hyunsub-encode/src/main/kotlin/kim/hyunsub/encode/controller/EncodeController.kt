package kim.hyunsub.encode.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.encode.model.EncodeParams
import kim.hyunsub.encode.service.EncodeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_encode"])
@RestController
@RequestMapping("/api/v1/encode")
class EncodeController(
	private val encodeService: EncodeService,
) {
	companion object : Log

	@PostMapping("")
	fun encode(@RequestBody params: EncodeParams): SimpleResponse {
		log.info("[EncodeController] encode: params={}", params)
		encodeService.pushToQueue(params)
		return SimpleResponse()
	}
}
