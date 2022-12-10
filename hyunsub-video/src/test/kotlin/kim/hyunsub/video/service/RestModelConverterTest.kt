package kim.hyunsub.video.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoSubtitle

class RestModelConverterTest: FreeSpec({
	val fileUrlConverter = mockk<FileUrlConverter>()
	val service = ApiModelConverter(fileUrlConverter)

	beforeSpec {
		every { fileUrlConverter.pathToUrl(any()) } answers {
			"https://example.com${firstArg<String>()}"
		}
	}

	"convertVideoSubtitle" - {
		val video = mockk<Video>(relaxed = true)
		val subtitle = mockk<VideoSubtitle>(relaxed = true)

		beforeTest {
			every { video.path } returns "/a/b/c.mp4"
		}

		"empty" {
			every { subtitle.path } returns "/a/b/c.smi"

			val result = service.convertVideoSubtitle(video, subtitle)
			result.label shouldBe "Korean"
			result.srclang shouldBe "ko"
			result.url shouldBe "https://example.com/a/b/c.vtt"
		}

		"ko" {
			every { subtitle.path } returns "/a/b/c.ko.smi"

			val result = service.convertVideoSubtitle(video, subtitle)
			result.label shouldBe "Korean"
			result.srclang shouldBe "ko"
			result.url shouldBe "https://example.com/a/b/c.ko.vtt"
		}

		"ko2" {
			every { subtitle.path } returns "/a/b/c.ko2.smi"

			val result = service.convertVideoSubtitle(video, subtitle)
			result.label shouldBe "Korean 2"
			result.srclang shouldBe "ko2"
			result.url shouldBe "https://example.com/a/b/c.ko2.vtt"
		}

		"en" {
			every { subtitle.path } returns "/a/b/c.en.smi"

			val result = service.convertVideoSubtitle(video, subtitle)
			result.label shouldBe "English"
			result.srclang shouldBe "en"
			result.url shouldBe "https://example.com/a/b/c.en.vtt"
		}

		"en2" {
			every { subtitle.path } returns "/a/b/c.en2.smi"

			val result = service.convertVideoSubtitle(video, subtitle)
			result.label shouldBe "English 2"
			result.srclang shouldBe "en2"
			result.url shouldBe "https://example.com/a/b/c.en2.vtt"
		}
	}
})
