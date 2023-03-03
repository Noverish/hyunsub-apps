package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.model.RestApiApparelPreview
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.service.ApiModelConverter
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/brands")
class ApparelBrandController(
	private val apparelRepository: ApparelRepository,
	private val apparelPreviewRepository: ApparelPreviewRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@GetMapping("")
	fun brands(userAuth: UserAuth): List<String> {
		return apparelRepository.findBrands(userAuth.idNo).filterNotNull().sorted()
	}

	@GetMapping("/{brand}/apparels")
	fun brandApparels(
		userAuth: UserAuth,
		@RequestParam p: Int,
		@PathVariable brand: String,
	): RestApiPageResult<RestApiApparelPreview> {
		val userId = userAuth.idNo

		val total = apparelPreviewRepository.countByBrandAndUserId(brand, userId)
		val pageRequest = PageRequest.of(p, 48)

		val list = apparelPreviewRepository.findByBrandAndUserId(brand, userAuth.idNo, pageRequest)
			.map { apiModelConverter.convert(userAuth.idNo, it) }

		return RestApiPageResult(total, p, 48, list)
	}
}
