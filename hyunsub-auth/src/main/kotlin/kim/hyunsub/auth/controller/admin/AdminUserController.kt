package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.dto.ModifyUserAuthorityParams
import kim.hyunsub.auth.model.user.ApiUser
import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.UserAuthority
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse
import mu.KotlinLogging
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/users")
class AdminUserController(
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
) {
	private val log = KotlinLogging.logger { }

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
