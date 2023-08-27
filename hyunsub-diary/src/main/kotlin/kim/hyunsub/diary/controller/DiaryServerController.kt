package kim.hyunsub.diary.controller

import kim.hyunsub.common.config.AppConstants
import kim.hyunsub.common.fs.model.UserDeleteParams
import kim.hyunsub.common.fs.model.UserInitParams
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.SimpleResponse2
import kim.hyunsub.diary.repository.DiaryRepository
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Authorized(["server"])
@RestController
@RequestMapping("/api/v1/server")
class DiaryServerController(
	private val diaryRepository: DiaryRepository,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/user/init")
	fun userInit(@RequestBody params: UserInitParams): SimpleResponse2 {
		log.debug { "[User Init] params=$params" }

		val fromUserId = AppConstants.INIT_FROM_USER_ID
		val toUserId = params.userId

		val diary = diaryRepository.findByUserId(fromUserId).first()
		log.debug { "[User Init] diary=$diary" }

		val newDiary = diary.copy(userId = toUserId, date = LocalDate.now())
		log.debug { "[User Init] newDiary=$newDiary" }

		if (!params.dryRun) {
			diaryRepository.save(newDiary)
		}

		return SimpleResponse2()
	}

	@PostMapping("/user/delete")
	fun userDelete(@RequestBody params: UserDeleteParams): SimpleResponse2 {
		log.debug { "[User Delete] params=$params" }

		val userId = params.userId

		val diaries = diaryRepository.findByUserId(userId)
		log.debug { "[User Delete] diaries=$diaries" }

		if (!params.dryRun) {
			diaryRepository.deleteAll(diaries)
		}

		return SimpleResponse2()
	}
}
