package kim.hyunsub.apparel.controller

import kim.hyunsub.apparel.config.ApparelConstants
import kim.hyunsub.apparel.model.ApiApparel
import kim.hyunsub.apparel.model.ApiApparelPreview
import kim.hyunsub.apparel.model.toApi
import kim.hyunsub.apparel.model.toApiInfo
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelPreviewRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/apparels")
class ApparelController(
	private val apparelRepository: ApparelRepository,
	private val apparelPreviewRepository: ApparelPreviewRepository,
	private val apparelImageRepository: ApparelImageRepository,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam(defaultValue = "0") p: Int,
	): ApiPageResult<ApiApparelPreview> {
		val userId = userAuth.idNo

		val total = apparelPreviewRepository.countByUserId(userId)
		val pageRequest = PageRequest.of(p, ApparelConstants.PAGE_SIZE)
		val list = apparelPreviewRepository.findByUserId(userId, pageRequest)
			.map { it.toApi(userId) }

		return ApiPageResult(total, p, ApparelConstants.PAGE_SIZE, list)
	}

	@GetMapping("/{apparelId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable apparelId: String,
	): ApiApparel {
		val userId = userAuth.idNo

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val images = apparelImageRepository.findByApparelIdOrderByRegDt(apparelId)

		return ApiApparel(
			id = apparelId,
			info = apparel.toApiInfo(),
			images = images.map { it.toApi(userId) }
		)
	}
}
