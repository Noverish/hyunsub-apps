package kim.hyunsub.auth.controller

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.model.MyPageUserInfo
import kim.hyunsub.auth.model.dto.ModifyUserInfoParams
import kim.hyunsub.auth.model.dto.ModifyUserInfoResult
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.auth.service.RsaKeyService
import kim.hyunsub.common.log.Log
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
	private val rsaKeyService: RsaKeyService,
	private val userRepository: UserRepository,
) {
	companion object : Log

	@PutMapping("")
	fun updateUserInfo(
		user: User,
		@RequestBody params: ModifyUserInfoParams,
	): ModifyUserInfoResult {
		log.debug("updateUserInfo: user={}, params={}", user, params)

		var newUser = user

		params.username?.let { username ->
			val decrypted = rsaKeyService.decrypt(username)
			newUser = newUser.copy(username = decrypted)
		}

		params.password?.let { password ->
			val decrypted = rsaKeyService.decrypt(password)
			log.debug("updateUserInfo: password={}", decrypted)
			val hashed = BCrypt.withDefaults().hashToString(AuthConstants.BCRYPT_COST, decrypted.toCharArray())
			newUser = newUser.copy(password = hashed)
		}

		val result = ModifyUserInfoResult(
			username = user.username != newUser.username,
			password = user.password != newUser.password,
		)

		if (newUser != user) {
			log.debug("updateUserInfo: newUser={}", newUser)
			userRepository.save(newUser)
		}

		return result
	}

	@GetMapping("/my-page")
	fun getMyPageUserInfo(user: User): MyPageUserInfo {
		return MyPageUserInfo(
			username = user.username,
			historyNum = 0,
			deviceNum = 0,
		)
	}
}
