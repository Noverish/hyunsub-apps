package kim.hyunsub.auth.controller

import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.model.RegisterResult
import kim.hyunsub.auth.service.RegisterService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/register")
class RegisterApiController(
	private val registerService: RegisterService,
) {
	@PostMapping("")
	fun register(@RequestBody params: RegisterParams): RegisterResult {
		return registerService.register(params)
	}
}
