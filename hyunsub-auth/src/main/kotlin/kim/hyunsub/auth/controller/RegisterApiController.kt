package kim.hyunsub.auth.controller

import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.model.RegisterResult
import kim.hyunsub.auth.service.RegisterService
import kim.hyunsub.auth.service.RsaKeyService
import kim.hyunsub.util.log.Log
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/register")
class RegisterApiController(
	private val registerService: RegisterService,
	private val rsaKeyService: RsaKeyService,
) {
	companion object : Log

	@PostMapping("")
	fun register(@RequestBody params: RegisterParams): RegisterResult {
		log.debug("register: params={}", params)
		val decryptedParams = params.copy(
			username = rsaKeyService.decrypt(params.username),
			password = rsaKeyService.decrypt(params.password),
		)
		log.debug("register: decryptedParams={}", decryptedParams)

		return registerService.register(decryptedParams)
	}
}
