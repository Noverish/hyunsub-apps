package kim.hyunsub.diary.service

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsFileClient
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.io.path.Path
import kotlin.random.Random

@Service
class DiaryDummyService(
	private val diaryRepository: DiaryRepository,
	private val fsClient: FsClient,
	private val fsFileClient: FsFileClient,
) {
	private val userIds = listOf("00001", "9qs7H")
	private val log = KotlinLogging.logger { }

	@Scheduled(cron = "0 0 6 * * *")
	fun saveDummy() {
		userIds.forEach { saveDummyWithUserId(it) }
	}

	private fun saveDummyWithUserId(userId: String) {
		log.debug { "[Diary Dummy] userId=$userId" }

		val path = "/hyunsub/drive/99999/novels"
		val novel = fsClient.readdir(path).random()
		val content = fsFileClient.readAsString(Path(path, novel).toString())
		val parts = content.split(" ")
		val size = Random.nextInt(100, 300)
		val i = Random.nextInt(parts.size - size)

		val diary = Diary(
			userId = userId,
			date = LocalDate.now(),
			summary = "",
			content = parts.subList(i, i + size).joinToString(" ")
		)

		diaryRepository.save(diary)
	}
}
