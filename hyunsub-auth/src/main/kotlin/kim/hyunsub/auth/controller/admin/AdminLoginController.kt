package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.service.CookieGenerator
import kim.hyunsub.auth.service.TokenService
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/users/{idNo}/login")
class AdminLoginController(
	private val userRepository: UserRepository,
	private val tokenService: TokenService,
	private val cookieGenerator: CookieGenerator,
) {
	@PostMapping("")
	fun login(response: HttpServletResponse, @PathVariable idNo: String): SimpleResponse2 {
		val user = userRepository.findByIdOrNull(idNo)
			?: throw ErrorCodeException(ErrorCode.NOT_EXIST_USER)
		val token = tokenService.issue(user)
		val cookie = cookieGenerator.generateLoginCookie(token, true)
		response.addCookie(cookie)
		return SimpleResponse2()
	}
}
