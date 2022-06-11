package kim.hyunsub.auth.controller

import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.model.RegisterResult
import kim.hyunsub.auth.service.RegisterService
import kim.hyunsub.util.log.Log
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/register")
class RegisterApiController(
	private val registerService: RegisterService,
) {
	companion object : Log

	@PostMapping("")
	fun register(@RequestBody params: RegisterParams): RegisterResult {
		log.debug("register: {}", params)
		return registerService.register(params)
	}
}
