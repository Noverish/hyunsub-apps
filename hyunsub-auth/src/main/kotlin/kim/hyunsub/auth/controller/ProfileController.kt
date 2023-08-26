package kim.hyunsub.auth.controller

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.model.Profile
import kim.hyunsub.auth.model.dto.ModifyUserInfoParams
import kim.hyunsub.auth.model.dto.ProfileUpdateResult
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.auth.service.RsaKeyService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(
	private val rsaKeyService: RsaKeyService,
	private val userRepository: UserRepository,
) {
	private val log = KotlinLogging.logger { }

	@PutMapping("")
	fun update(
		user: User,
		@RequestBody params: ModifyUserInfoParams,
	): ProfileUpdateResult {
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

		val result = ProfileUpdateResult(
			username = user.username != newUser.username,
			password = user.password != newUser.password,
		)

		if (newUser != user) {
			log.debug("updateUserInfo: newUser={}", newUser)
			userRepository.save(newUser)
		}

		return result
	}

	@GetMapping("")
	fun detail(user: User): Profile {
		return Profile(
			username = user.username,
			historyNum = 0,
			deviceNum = 0,
		)
	}
}
