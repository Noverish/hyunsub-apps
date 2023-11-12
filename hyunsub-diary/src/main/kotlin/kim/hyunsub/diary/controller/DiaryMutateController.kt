package kim.hyunsub.diary.controller

import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.bo.DiaryMutateBo
import kim.hyunsub.diary.model.api.ApiDiary
import kim.hyunsub.diary.model.dto.DiaryCreateParams
import kim.hyunsub.diary.model.dto.DiaryUpdateParams
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/diaries")
class DiaryMutateController(
	private val diaryMutateBo: DiaryMutateBo,
) {
	@PostMapping("")
	fun create(
		user: UserAuth,
		@RequestBody params: DiaryCreateParams,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary {
		return diaryMutateBo.create(user.idNo, token, params)
	}

	@PutMapping("/{date}")
	fun update(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@RequestBody params: DiaryUpdateParams,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary {
		return diaryMutateBo.update(user.idNo, token, date, params)
	}

	@DeleteMapping("/{date}")
	fun delete(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary {
		return diaryMutateBo.delete(user.idNo, token, date)
	}
}
