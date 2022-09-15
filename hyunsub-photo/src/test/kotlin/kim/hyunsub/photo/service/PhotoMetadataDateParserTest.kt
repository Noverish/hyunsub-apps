package kim.hyunsub.photo.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class PhotoMetadataDateParserTest: FreeSpec({
	val parser = PhotoMetadataDateParser()

	"parse" {
		parser.parse("2020:07:19 05:59:03-07:00") shouldBe LocalDateTime.parse("2020-07-19T21:59:03")
		parser.parse("2020:07:19 12:35:03.123+00:00") shouldBe LocalDateTime.parse("2020-07-19T21:35:03.123")
		parser.parse("2020:08:27 19:21:18.1152") shouldBe LocalDateTime.parse("2020-08-27T19:21:18.1152")
		parser.parse("2019:09:21 10:31:21") shouldBe LocalDateTime.parse("2019-09-21T10:31:21")
		parser.parse("2019-09-21 10:31:21") shouldBe LocalDateTime.parse("2019-09-21T10:31:21")
		parser.parse("2020:08:27 09:21:18.1152", "+05:00") shouldBe LocalDateTime.parse("2020-08-27T13:21:18.1152")
		parser.parse("2019:09:21 10:31:21", "-01:00") shouldBe LocalDateTime.parse("2019-09-21T20:31:21")
		parser.parse("2019-09-21 10:31:21", "-01:00") shouldBe LocalDateTime.parse("2019-09-21T20:31:21")
	}

	"parseFromFileName" {
		parser.parseFromFileName("1576233363243.jpg") shouldBe LocalDateTime.parse("2019-12-13T19:36:03.243")
		parser.parseFromFileName("1576233363243-0.jpg") shouldBe LocalDateTime.parse("2019-12-13T19:36:03.243")
		parser.parseFromFileName("20191114_182427.jpg") shouldBe LocalDateTime.parse("2019-11-14T18:24:27")
		parser.parseFromFileName("20191114_182427(0).jpg") shouldBe LocalDateTime.parse("2019-11-14T18:24:27")
		parser.parseFromFileName("B612_20190611_213432_679.jpg") shouldBe LocalDateTime.parse("2019-06-11T21:34:32.679")
		parser.parseFromFileName("beauty_20190917203531.jpg") shouldBe LocalDateTime.parse("2019-09-17T20:35:31")
		parser.parseFromFileName("CAP_20190911_174209.jpg") shouldBe LocalDateTime.parse("2019-09-11T17:42:09")
		parser.parseFromFileName("Screenshot_20191114-212728_KakaoTalk.jpg") shouldBe LocalDateTime.parse("2019-11-14T21:27:28")
	}
})
