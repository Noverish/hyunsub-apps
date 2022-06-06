package kim.hyunsub.auth.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kim.hyunsub.auth.exception.ErrorCodeException
import kim.hyunsub.auth.model.ErrorCode
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.model.LoginResult
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User

class LoginServiceTest : FreeSpec({
	val userRepository = mockk<UserRepository>()
	val jwtService = mockk<JwtService>()
	val service = LoginService(userRepository, jwtService)

	val user = mockk<User>()
	val params = mockk<LoginParams>()

	val idNo = "kotest_id_no"
	val username = "kotest_username"
	val password = "kotest_password"
	val hashed = "\$2a\$12\$umWkbsinybLfpuEknH3IqufPEtv8hbUYJts6GN/fBAZZA.fIpp1aK"
	val jwt = "kotest_jwt"

	beforeTest {
		every { user.idNo } returns idNo
		every { user.password } returns hashed
		every { params.username } returns username
		every { params.password } returns password

		every { userRepository.findByUsername(username) } returns user
		every { jwtService.issue(any()) } returns jwt
	}

	"Success" {
		val result = service.login(params)
		result shouldBe LoginResult(idNo, jwt)
	}

	"Not exist user" {
		every { userRepository.findByUsername(username) } returns null
		val ex = shouldThrow<ErrorCodeException> { service.login(params) }
		ex.errorCode shouldBe ErrorCode.NOT_EXIST_USER
	}

	"Incorrect password" {
		every { user.password } returns "wrong_hashed"
		val ex = shouldThrow<ErrorCodeException> { service.login(params) }
		ex.errorCode shouldBe ErrorCode.NOT_EXIST_USER
	}
})
