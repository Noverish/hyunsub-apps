package kim.hyunsub.auth.controller

import kim.hyunsub.auth.service.LogoutService
import kim.hyunsub.auth.service.UserService
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/sign-out")
class SignOutController(
	private val userService: UserService,
	private val logoutService: LogoutService,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("")
	fun signOut(
		userAuth: UserAuth,
		response: HttpServletResponse,
	): SimpleResponse {
		val idNo = userAuth.idNo
		log.info("[SignOut] idNo={}", idNo)

		userService.delete(idNo)
		logoutService.logout(response)

		return SimpleResponse()
	}
}
