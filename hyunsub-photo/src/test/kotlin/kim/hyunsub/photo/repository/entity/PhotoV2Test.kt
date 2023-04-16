package kim.hyunsub.photo.repository.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class PhotoV2Test : FreeSpec({
	"generateId" {
		val millis = 1677996992451
		val hash = "nPc4BlitYUzWqSO_uwWnHAbBWphC-Pv1FN2Heetzqnc="

		val id = Photo.generateId(millis, hash)
		id shouldBe "186b06a27c3nPc4B"

		val restored = Photo.restoreMillis(id)
		restored shouldBe millis
	}
})

// 1677996992451
// 000 001 86b 06a 27c 3
// AA  AB  hr  Bq  J8  M=
