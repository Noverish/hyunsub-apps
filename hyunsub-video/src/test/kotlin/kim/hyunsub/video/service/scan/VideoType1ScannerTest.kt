package kim.hyunsub.video.service.scan

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kim.hyunsub.common.api.model.FileStat
import kim.hyunsub.common.jackson.JacksonConfiguration
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.video.model.VideoScanResult
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger

class VideoType1ScannerTest : FreeSpec({
	val category = "kotest_category"
	val randomGenerator = mockk<RandomGenerator>()

	val ai = AtomicInteger(0)
	every { randomGenerator.generateRandomString(6) } answers {
		ai.getAndAdd(1).toString().padStart(6, '0')
	}

	"test" {
		val mapper = JacksonConfiguration().objectMapper()
		val paramsJson = String(Files.readAllBytes(Paths.get(ClassPathResource("VideoType1Scanner_Params.json").uri)))
		val files = mapper.readValue<List<FileStat>>(paramsJson)

		val scanner = VideoType1Scanner(randomGenerator, category, files)
		val result = scanner.scan("/root")

		val resultJson = String(Files.readAllBytes(Paths.get(ClassPathResource("VideoType1Scanner_Result.json").uri)))
		val expected = mapper.readValue<VideoScanResult>(resultJson)

		result shouldBe expected
	}
})
