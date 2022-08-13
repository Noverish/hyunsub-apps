package kim.hyunsub.auth.controller.admin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.auth.model.token.TokenIssueApiParams
import kim.hyunsub.auth.model.token.TokenVerifyApiParams
import kim.hyunsub.auth.service.JwtService
import kim.hyunsub.auth.service.TokenGenerator
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.originalIp
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

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
	fun encrypt(request: HttpServletRequest, @RequestBody params: TokenIssueApiParams): String {
		val idNo = params.idNo
		log.info("JWT Issue: {} - {}", request.originalIp, idNo)
		return tokenGenerator.generateToken(idNo)
	}

	@PostMapping("/verify")
	fun decrypt(request: HttpServletRequest, @RequestBody params: TokenVerifyApiParams): String {
		val token = params.token
		log.info("JWT Verify: {} - {}", request.originalIp, token)
		return mapper.writeValueAsString(jwtService.verify(token))
	}
}
