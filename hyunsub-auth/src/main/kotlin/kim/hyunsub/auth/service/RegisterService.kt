package kim.hyunsub.auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import kim.hyunsub.auth.exception.ErrorCodeException
import kim.hyunsub.auth.model.ErrorCode
import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.model.RegisterResult
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import org.springframework.stereotype.Service

@Service
class RegisterService(
	private val userRepository: UserRepository,
) {
	companion object {
		const val BCRYPT_COST = 12
	}

	fun register(params: RegisterParams): RegisterResult {
		val user = userRepository.findByUsername(params.username)
		if (user != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST_USERNAME)
		}

		val idNo = generateIdNo()
		val hashed = BCrypt.withDefaults().hashToString(BCRYPT_COST, params.password.toCharArray())
		val newUser = User(idNo = idNo, username = params.username, password = hashed)
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
