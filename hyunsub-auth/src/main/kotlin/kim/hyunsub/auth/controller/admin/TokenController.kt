package kim.hyunsub.auth.controller.admin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.auth.model.token.TokenIssueApiParams
import kim.hyunsub.auth.model.token.TokenVerifyApiParams
import kim.hyunsub.auth.service.JwtService
import kim.hyunsub.auth.service.TokenGenerator
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/token")
class TokenController(
	private val jwtService: JwtService,
	private val tokenGenerator: TokenGenerator,
) {
	companion object : Log
	private val mapper = jacksonObjectMapper()

	@PostMapping("/issue")
	fun encrypt(@RequestBody params: TokenIssueApiParams): String {
		log.info("JWT Issue: {}", params)
		val duration = Duration.parse(params.duration)
		return tokenGenerator.generateToken(params.idNo, duration)
	}

	@PostMapping("/verify")
	fun decrypt(@RequestBody params: TokenVerifyApiParams): String {
		log.info("JWT Verify: {}", params)
		return mapper.writeValueAsString(jwtService.verify(params.token))
	}
}
