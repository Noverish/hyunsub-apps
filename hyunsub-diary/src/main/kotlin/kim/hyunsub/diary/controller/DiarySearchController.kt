package kim.hyunsub.diary.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class DiarySearchController(
	private val diaryRepository: DiaryRepository,
) {
	@GetMapping("")
	fun search(
		user: UserAuth,
		q: String,
	): List<Diary> {
		return diaryRepository.findByUserIdAndContentContains(user.idNo, q)
	}
}
