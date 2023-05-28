package kim.hyunsub.auth.controller.admin

import kim.hyunsub.auth.model.dto.UserCreateParams
import kim.hyunsub.auth.model.user.ApiUser
import kim.hyunsub.auth.model.user.toDto
import kim.hyunsub.auth.repository.UserAuthorityRepository
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.auth.repository.generateId
import kim.hyunsub.common.fs.FsClient
import kim.hyunsub.common.fs.model.FsRsyncParams
import kim.hyunsub.common.web.annotation.Authorized
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin/users")
class AdminUserController(
	private val fsClient: FsClient,
	private val userRepository: UserRepository,
	private val userAuthorityRepository: UserAuthorityRepository,
) {
	@GetMapping("")
	fun userList(): List<ApiUser> {
		val users = userRepository.findAll()
		val userAuthorities = userAuthorityRepository.findAll()

		val authorityMap = userAuthorities.groupBy({ it.userIdNo }, { it.authorityId })

		return users.map { user ->
			user.toDto(authorityMap[user.idNo] ?: emptyList())
		}
	}

	@PostMapping("")
	fun createUser(@RequestBody params: UserCreateParams): ApiUser {
		val user = User(
			idNo = userRepository.generateId(),
			username = params.name,
			password = "password",
		)

		fsClient.rsync(
			FsRsyncParams(
				from = "/hyunsub/drive/base",
				to = "/hyunsub/drive/${user.idNo}",
			)
		)

		return user.toDto()
	}
}
