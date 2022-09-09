package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.user.ApiAuthority
import kim.hyunsub.auth.repository.AuthorityRepository
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/authorities")
class AdminAuthorityController(
	private val authorityRepository: AuthorityRepository,
) {
	companion object : Log

	@GetMapping("")
	fun authorities(): List<ApiAuthority> {
		return authorityRepository.findAll()
			.map { ApiAuthority(it.id, it.name) }
	}
}
