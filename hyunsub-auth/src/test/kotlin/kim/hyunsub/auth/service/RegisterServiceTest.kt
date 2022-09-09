package kim.hyunsub.auth.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kim.hyunsub.auth.model.RegisterParams
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException

class RegisterServiceTest: FreeSpec({
	val userRepository = mockk<UserRepository>()
	val captchaService = mockk<CaptchaService>()
	val service = RegisterService(userRepository, captchaService)

	val slot = slot<User>()
	val params = mockk<RegisterParams>()
	val username = "kotest_username"
	val password = "kotest_password"

	beforeTest {
		every { params.username } returns username
		every { params.password } returns password
		every { userRepository.findByUsername(username) } returns null
		every { userRepository.existsById(any()) } returns false
		every { userRepository.saveAndFlush(capture(slot)) } answers { slot.captured }
	}

	"Success" {
		val result = service.register(params)
		result.idNo shouldNotBe null
	}

	"Already exist username" {
		every { userRepository.findByUsername(username) } returns mockk()
		val ex = shouldThrow<ErrorCodeException> { service.register(params) }
		ex.errorCode shouldBe ErrorCode.ALREADY_EXIST_USERNAME
	}
})
