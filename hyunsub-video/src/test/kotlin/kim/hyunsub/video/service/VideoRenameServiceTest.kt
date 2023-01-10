package kim.hyunsub.video.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.video.model.VideoRenameParams
import kim.hyunsub.video.repository.*

class VideoRenameServiceTest : FreeSpec({
	val apiCaller: ApiCaller = mockk()
	val videoRepository: VideoRepository = mockk()
	val videoEntryRepository: VideoEntryRepository = mockk()
	val videoMetadataRepository: VideoMetadataRepository = mockk()
	val videoSubtitleRepository: VideoSubtitleRepository = mockk()
	val videoRenameHistoryRepository: VideoRenameHistoryRepository = mockk()
	val service = VideoRenameService(
		apiCaller,
		videoRepository,
		videoEntryRepository,
		videoMetadataRepository,
		videoSubtitleRepository,
		videoRenameHistoryRepository
	)

	"replace" {
		val origin = "/a/b/c/Test 001/Test 001.mp4"
		val expected = "/a/b/c/Test 001/Test 002.mp4"

		val params = VideoRenameParams(
			videoId = "abcdef",
			from = "Test 001",
			to = "Test 002",
			isRegex = false,
		)

		service.replace(origin, params) shouldBe expected
	}
})
