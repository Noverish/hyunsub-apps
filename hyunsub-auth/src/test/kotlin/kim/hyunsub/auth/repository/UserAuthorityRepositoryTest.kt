package kim.hyunsub.auth.repository

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kim.hyunsub.auth.HyunsubAuthApplication
import kim.hyunsub.auth.repository.entity.UserAuthority
import kim.hyunsub.auth.repository.entity.UserAuthorityId
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(classes = [HyunsubAuthApplication::class])
class UserAuthorityRepositoryTest(
	userAuthorityRepository: UserAuthorityRepository
): FreeSpec({
	val userIdNo = "12345"
	val authorityId = 12345

	"Select, Insert, Select, Delete, Select" {
		// Select
		userAuthorityRepository.findByIdOrNull(UserAuthorityId(userIdNo, authorityId)) shouldBe null

		// Insert
		val userAuthority = UserAuthority(userIdNo, authorityId)
		userAuthorityRepository.saveAndFlush(userAuthority)

		// Select
		userAuthorityRepository.findByIdOrNull(UserAuthorityId(userIdNo, authorityId)) shouldBe userAuthority

		// Delete
		userAuthorityRepository.delete(userAuthority)

		// Select
		userAuthorityRepository.findByIdOrNull(UserAuthorityId(userIdNo, authorityId)) shouldBe null
	}
})
