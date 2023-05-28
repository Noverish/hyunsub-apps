package kim.hyunsub.auth.controller.auth

import kim.hyunsub.auth.model.RegisterApiParams
import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.model.RegisterResult
import kim.hyunsub.auth.service.RegisterService
import kim.hyunsub.auth.service.RsaKeyService
import kim.hyunsub.common.web.annotation.IgnoreAuthorize
import kim.hyunsub.common.web.model.HyunsubHeader
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@IgnoreAuthorize
@RestController
@RequestMapping("/api/v1/auth/register")
class RegisterController(
	private val registerService: RegisterService,
	private val rsaKeyService: RsaKeyService,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("")
	fun register(
		@RequestHeader(HyunsubHeader.X_ORIGINAL_IP) remoteAddr: String,
		@RequestBody params: RegisterApiParams,
	): RegisterResult {
		log.debug("register: params={}", params)
		val decryptedParams = RegisterParams(
			username = rsaKeyService.decrypt(params.username),
			password = rsaKeyService.decrypt(params.password),
			captcha = params.captcha,
			remoteAddr = remoteAddr,
		)
		log.debug("register: decryptedParams={}", decryptedParams)

		return registerService.register(decryptedParams)
	}
}
