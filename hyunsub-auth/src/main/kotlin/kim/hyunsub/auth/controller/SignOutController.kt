package kim.hyunsub.auth.controller

import kim.hyunsub.auth.service.LogoutService
import kim.hyunsub.auth.service.SignOutService
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/sign-out")
class SignOutController(
	private val signOutService: SignOutService,
	private val logoutService: LogoutService,
) {
	companion object : Log

	@PostMapping("")
	fun signOut(
		userAuth: UserAuth,
		response: HttpServletResponse,
	): SimpleResponse {
		val idNo = userAuth.idNo
		log.info("[SignOut] idNo={}", idNo)

		signOutService.signOut(idNo)
		logoutService.logout(response)

		return SimpleResponse()
	}
}
