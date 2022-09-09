package kim.hyunsub.auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.model.RegisterResult
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.stereotype.Service

@Service
class RegisterService(
	private val userRepository: UserRepository,
	private val captchaService: CaptchaService,
) {
	companion object : Log

	fun register(params: RegisterParams): RegisterResult {
		val captchaSuccess = captchaService.verify(params.captcha, params.remoteAddr)
		if (!captchaSuccess) {
			throw ErrorCodeException(ErrorCode.CAPTCHA_FAILURE)
		}

		val user = userRepository.findByUsername(params.username)
		if (user != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST_USERNAME)
		}

		if (params.username.length < 4) {
			throw ErrorCodeException(ErrorCode.SHORT_USERNAME)
		}

		if (params.password.length < 8) {
			throw ErrorCodeException(ErrorCode.SHORT_PASSWORD)
		}

		val idNo = generateIdNo()
		val hashed = BCrypt.withDefaults().hashToString(AuthConstants.BCRYPT_COST, params.password.toCharArray())
		val newUser = User(idNo = idNo, username = params.username, password = hashed)
		log.debug("newUser: {}", newUser)
		userRepository.saveAndFlush(newUser)

		return RegisterResult(idNo)
	}

	fun generateIdNo(): String {
		while (true) {
			val idNo = IdNoGenerator.generate()
			if (!userRepository.existsById(idNo)) {
				return idNo
			}
		}
	}
}
