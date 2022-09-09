package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.admin.ModifyUserAuthorityParams
import kim.hyunsub.auth.model.user.ApiUser
import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.UserAuthority
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse
import org.springframework.web.bind.annotation.*

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/users")
class AdminUserController(
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
) {
	companion object : Log

	@GetMapping("")
	fun userList(): List<ApiUser> {
		val users = userRepository.findAll()
		val userAuthorities = userAuthorityRepository.findAll()

		return users.map { user ->
			val authorities = userAuthorities.filter { it.userIdNo == user.idNo }
				.map { it.authorityId }
			ApiUser(user.idNo, user.username, authorities)
		}
	}

	@PutMapping("/authority")
	fun putUserAuthority(@RequestBody params: ModifyUserAuthorityParams): SimpleResponse {
		log.debug("putUserAuthority: {}", params)
		val userAuthority = UserAuthority(params.idNo, params.authorityId)
		userAuthorityRepository.save(userAuthority)
		return SimpleResponse()
	}

	@DeleteMapping("/authority")
	fun delUserAuthority(@RequestBody params: ModifyUserAuthorityParams): SimpleResponse {
		log.debug("delUserAuthority: {}", params)
		val userAuthority = UserAuthority(params.idNo, params.authorityId)
		userAuthorityRepository.delete(userAuthority)
		return SimpleResponse()
	}
}
