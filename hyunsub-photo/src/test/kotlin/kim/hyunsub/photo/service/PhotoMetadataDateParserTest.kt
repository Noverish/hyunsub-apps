package kim.hyunsub.photo.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class PhotoMetadataDateParserTest: FreeSpec({
	"parse" {
		PhotoMetadataDateParser.parse("2020:07:19 05:59:03-07:00") shouldBe LocalDateTime.of(2020, 7, 19, 21, 59, 3)
		PhotoMetadataDateParser.parse("2020:07:19 12:35:03.123+00:00") shouldBe LocalDateTime.of(2020, 7, 19, 21, 35, 3, 123000000)
		PhotoMetadataDateParser.parse("2020:08:27 19:21:18.1152") shouldBe LocalDateTime.of(2020, 8, 27, 19, 21, 18, 115200000)
		PhotoMetadataDateParser.parse("2019:09:21 10:31:21") shouldBe LocalDateTime.of(2019, 9, 21, 10, 31, 21)
		PhotoMetadataDateParser.parse("20190921_103121") shouldBe LocalDateTime.of(2019, 9, 21, 10, 31, 21)
	}
})
