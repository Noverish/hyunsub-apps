package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.ApiApparelPreview
import kim.hyunsub.apparel.model.toApi
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/brands")
class ApparelBrandController(
	private val apparelRepository: ApparelRepository,
	private val apparelPreviewRepository: ApparelPreviewRepository,
) {
	@GetMapping("")
	fun brands(userAuth: UserAuth): List<String> {
		return apparelRepository.findBrands(userAuth.idNo).filterNotNull().sorted()
	}

	@GetMapping("/{brand}/apparels")
	fun brandApparels(
		userAuth: UserAuth,
		@RequestParam(defaultValue = "0") p: Int,
		@PathVariable brand: String,
	): ApiPageResult<ApiApparelPreview> {
		val userId = userAuth.idNo

		val total = apparelPreviewRepository.countByBrandAndUserId(brand, userId)
		val pageRequest = PageRequest.of(p, 48)

		val list = apparelPreviewRepository.findByBrandAndUserId(brand, userAuth.idNo, pageRequest)
			.map { it.toApi(userId) }

		return ApiPageResult(total, p, 48, list)
	}
}
