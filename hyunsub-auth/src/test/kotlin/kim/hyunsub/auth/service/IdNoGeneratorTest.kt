package kim.hyunsub.auth.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class IdNoGeneratorTest: FreeSpec({
	"test" {
		for (i in 1..10) {
			val result = IdNoGenerator.generate()
			result.length shouldBe IdNoGenerator.ID_NO_LEN
		}
	}
})
