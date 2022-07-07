package kim.hyunsub.auth.controller.admin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.auth.service.JwtService
import kim.hyunsub.auth.service.TokenGenerator
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.isLocalhost
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/admin/token")
class TokenController(
	private val jwtService: JwtService,
	private val tokenGenerator: TokenGenerator,
) {
	companion object : Log
	private val mapper = jacksonObjectMapper()

	@PostMapping("/issue")
	fun encrypt(request: HttpServletRequest, @RequestParam idNo: String): String {
		log.info("JWT Issue: {} - {}", request.remoteAddr, idNo)
		return if (request.isLocalhost()) {
			return tokenGenerator.generateToken(idNo)
		} else {
			request.remoteAddr
		}
	}

	@PostMapping("/verify")
	fun decrypt(request: HttpServletRequest, @RequestParam token: String): String {
		log.info("JWT Verify: {} - {}", request.remoteAddr, token)
		return if (request.isLocalhost()) {
			mapper.writeValueAsString(jwtService.verify(token))
		} else {
			request.remoteAddr
		}
	}
}
