package kim.hyunsub.auth.controller

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.model.Profile
import kim.hyunsub.auth.model.dto.ProfileUpdateParams
import kim.hyunsub.auth.model.dto.ProfileUpdateResult
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.service.CookieGenerator
import kim.hyunsub.auth.service.RsaKeyService
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(
	private val rsaKeyService: RsaKeyService,
	private val userRepository: UserRepository,
	private val cookieGenerator: CookieGenerator,
) {
	private val log = KotlinLogging.logger { }

	@PutMapping("")
	fun update(
		userAuth: UserAuth,
		response: HttpServletResponse,
		@RequestBody params: ProfileUpdateParams,
	): ProfileUpdateResult {
		log.debug("updateUserInfo: userAuth={}, params={}", userAuth, params)

		val user = userRepository.findByIdOrNull(userAuth.idNo)
			?: throw ErrorCodeException(ErrorCode.NO_SUCH_USER)

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

		params.language?.let {
			newUser = newUser.copy(lang = it)
			val cookie = cookieGenerator.generateLanguageCookie(it)
			response.addCookie(cookie)
		}

		val result = ProfileUpdateResult(
			username = params.username?.let { user.username != newUser.username },
			password = params.password?.let { user.password != newUser.password },
			language = params.language?.let { user.lang != newUser.lang },
		)

		if (newUser != user) {
			log.debug("updateUserInfo: newUser={}", newUser)
			userRepository.save(newUser)
		}

		return result
	}

	@GetMapping("")
	fun detail(userAuth: UserAuth): Profile {
		val user = userRepository.findByIdOrNull(userAuth.idNo)
			?: throw ErrorCodeException(ErrorCode.NO_SUCH_USER)

		return Profile(
			username = user.username,
			historyNum = 0,
			deviceNum = 0,
			language = user.lang,
		)
	}
}
