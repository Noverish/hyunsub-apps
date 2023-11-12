package kim.hyunsub.diary.controller

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.bo.DiarySearchBo
import kim.hyunsub.diary.model.api.ApiDiaryPreview
import kim.hyunsub.diary.model.dto.DiarySearchParams
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class DiarySearchController(
	private val diarySearchBo: DiarySearchBo,
) {
	@PostMapping("")
	fun search(
		user: UserAuth,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@RequestBody(required = false) params: DiarySearchParams?,
	): ApiPageResult<ApiDiaryPreview> {
		return diarySearchBo.search(user.idNo, token, params)
	}
}
