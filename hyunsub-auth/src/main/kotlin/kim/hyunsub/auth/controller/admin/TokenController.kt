package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.dto.TokenIssueParams
import kim.hyunsub.auth.model.dto.TokenVerifyParams
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.service.TokenService
import kim.hyunsub.auth.service.UserAuthService
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/token")
class TokenController(
	private val tokenService: TokenService,
	private val userAuthService: UserAuthService,
	private val userRepository: UserRepository,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/issue")
	fun encrypt(@RequestBody params: TokenIssueParams): String {
		log.info { "[Token Issue] params=$params" }
		val duration = Duration.parse(params.duration)
		val user = userRepository.findByIdOrNull(params.idNo)
			?: throw ErrorCodeException(ErrorCode.NOT_EXIST_USER)
		log.info { "[Token Issue] user=$user" }
		val token = tokenService.issue(user, duration)
		log.info { "[Token Issue] token=$token" }
		return token
	}

	@PostMapping("/verify")
	fun decrypt(@RequestBody params: TokenVerifyParams): UserAuth {
		log.info { "[Token Verify] params=$params" }
		val tokenPayload = tokenService.verify(params.token)
		log.info { "[Token Verify] tokenPayload=$tokenPayload" }
		val userAuth = userAuthService.getUserAuth(tokenPayload.idNo)
		log.info { "[Token Verify] userAuth=$userAuth" }
		return userAuth
	}
}
