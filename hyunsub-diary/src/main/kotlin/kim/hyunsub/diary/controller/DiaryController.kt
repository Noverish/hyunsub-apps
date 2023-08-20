package kim.hyunsub.diary.controller

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.model.DiaryCreateParams
import kim.hyunsub.diary.model.DiaryModifyParams
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.diary.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/diaries")
class DiaryController(
	private val diaryRepository: DiaryRepository,
) {
	@PostMapping("")
	fun create(
		user: UserAuth,
		@RequestBody params: DiaryCreateParams,
	): Diary {
		if (diaryRepository.findByIdOrNull(user.idNo, params.date) != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
		}

		val diary = Diary(
			userId = user.idNo,
			date = params.date,
			summary = params.summary,
			content = params.content,
		)

		diaryRepository.save(diary)

		return diary
	}

	@GetMapping("/{date}")
	fun detail(
		user: UserAuth,
		@PathVariable date: LocalDate,
	): Diary {
		return diaryRepository.findByIdOrNull(user.idNo, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
	}

	@PutMapping("/{date}")
	fun modify(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@RequestBody params: DiaryModifyParams,
	): Diary {
		val diary = diaryRepository.findByIdOrNull(user.idNo, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newDiary = diary.copy(
			summary = params.summary ?: diary.summary,
			content = params.content ?: diary.content,
		)

		diaryRepository.save(newDiary)

		return newDiary
	}

	@DeleteMapping("/{date}")
	fun delete(
		user: UserAuth,
		@PathVariable date: LocalDate,
	): Diary {
		val diary = diaryRepository.findByIdOrNull(user.idNo, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		diaryRepository.delete(diary)

		return diary
	}
}
