package kim.hyunsub.auth.service

import com.navercorp.fixturemonkey.kotlin.giveMe
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kim.hyunsub.auth.config.TokenProperties
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.test.monkey
import java.time.Duration
import java.util.Base64

class TokenServiceTest : FreeSpec({
	val userAuthService: UserAuthService = mockk()
	val keyPair = CryptEccUtil.generateKeyPair()
	val tokenProperties = TokenProperties(
		private = Base64.getEncoder().encodeToString(keyPair.private.encoded),
		public = Base64.getEncoder().encodeToString(keyPair.public.encoded),
		duration = Duration.ofHours(1),
	)
	val service = TokenService(tokenProperties, userAuthService)

	val user = monkey.giveMeOne<User>()
	val authorities = monkey.giveMe<String>(5)

	beforeTest {
		every { userAuthService.getAuthorities(user.idNo) } returns authorities
	}

	"issue, verify" {
		val token = service.issue(user)
		val tokenPayload = service.verify(token)
		tokenPayload.idNo shouldBe user.idNo
		tokenPayload.username shouldBe user.username
		tokenPayload.authorities shouldBe authorities
	}
})
