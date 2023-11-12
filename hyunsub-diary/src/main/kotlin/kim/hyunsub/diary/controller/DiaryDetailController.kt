package kim.hyunsub.diary.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.bo.DiaryDetailBo
import kim.hyunsub.diary.model.api.ApiDiary
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/diaries/{date}")
class DiaryDetailController(
	private val diaryDetailBo: DiaryDetailBo,
) {
	@GetMapping("")
	fun detail(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary? {
		return diaryDetailBo.detail(user.idNo, token, date)
	}

	@GetMapping("/photos")
	fun photos(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@RequestParam(required = false) page: Int?,
	): RestApiPageResult<ApiPhotoPreview> {
		return diaryDetailBo.photos(token, date, page)
	}
}
