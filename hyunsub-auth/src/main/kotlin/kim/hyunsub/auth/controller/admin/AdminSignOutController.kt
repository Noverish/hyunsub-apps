package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.dto.AdminSignOutParams
import kim.hyunsub.auth.service.SignOutService
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/sign-out")
class AdminSignOutController(
	private val signOutService: SignOutService,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("")
	fun signOut(@RequestBody params: AdminSignOutParams): SimpleResponse {
		log.info("[Admin SignOut] params={}", params)
		signOutService.signOut(params.idNo)
		return SimpleResponse()
	}
}
