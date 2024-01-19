package kim.hyunsub.common.util

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class RandomsTest : FreeSpec({
	"generateRandomString" {
		generateRandomString(6, 0) shouldBe "jWcQFG"
	}

	"generateRandomNumber" {
		generateRandomNumber(6, 0) shouldBe "645589"
	}
})
