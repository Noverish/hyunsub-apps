package kim.hyunsub.auth.service

import kim.hyunsub.auth.exception.ErrorCodeException
import kim.hyunsub.auth.model.ErrorCode
import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegisterService(
	private val userRepository: UserRepository,
	private val passwordEncoder: PasswordEncoder,
) {
	fun register(params: RegisterParams) {
		val user = userRepository.findByUsername(params.username)
		if (user != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST_USERNAME)
		}

		val idNo = generateIdNo()
		val hashed = passwordEncoder.encode(params.password)
		val newUser = User(idNo = idNo, username = params.username, password = hashed)
		userRepository.saveAndFlush(newUser)
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
