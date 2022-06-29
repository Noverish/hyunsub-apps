package kim.hyunsub.auth.repository

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import kim.hyunsub.auth.HyunsubAuthApplication
import kim.hyunsub.auth.repository.entity.User
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(classes = [HyunsubAuthApplication::class])
class UserRepositoryTest(
	userRepository: UserRepository,
) : FreeSpec({
	val idNo = "12345"
	val username = "kotest_username"
	val password = "kotest_password"

	"Select, Insert, Select, Delete, Select" {
		// Select
		userRepository.findByIdOrNull(idNo) shouldBe null

		// Insert
		val user = User(idNo, username, password)
		userRepository.saveAndFlush(user)

		// Select
		userRepository.findByIdOrNull(idNo) shouldBe user
		userRepository.findByUsername(username) shouldBe user

		// Delete
		userRepository.delete(user)

		// Select
		userRepository.findByIdOrNull(idNo) shouldBe null
	}
})
