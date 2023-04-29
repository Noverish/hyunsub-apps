package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.service.ApiModelConverter
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class ApparelCategoryController(
	private val apparelRepository: ApparelRepository,
	private val apparelPreviewRepository: ApparelPreviewRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@GetMapping("")
	fun categories(userAuth: UserAuth): List<String> {
		return apparelRepository.findCategories(userAuth.idNo).sorted()
	}

	@GetMapping("/{category}/apparels")
	fun categoryApparels(
		userAuth: UserAuth,
		@RequestParam(defaultValue = "0") p: Int,
		@PathVariable category: String,
	): RestApiPageResult<RestApiApparelPreview> {
		val userId = userAuth.idNo

		val total = apparelPreviewRepository.countByCategoryAndUserId(category, userId)
		val pageRequest = PageRequest.of(p, 48)

		val list = apparelPreviewRepository.findByCategoryAndUserId(category, userAuth.idNo, pageRequest)
			.map { apiModelConverter.convert(userAuth.idNo, it) }

		return RestApiPageResult(total, p, 48, list)
	}
}
